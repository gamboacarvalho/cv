using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;
using Repository;
using Repository.DataObjets;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;
using TicketsOnline.Models;

namespace TicketsOnline.Controllers
{
    public class CartController : Controller
    {

        private readonly CartManager _cartManager;
        private readonly ISessionMapper _sessionMapper;
        private readonly IReserveMapper _reserveMapper;

        public CartController()
        {
            _cartManager = new CartManager(this);
            _sessionMapper = DataRepository.SessionMapperFactory.GetMapper();
            _reserveMapper = DataRepository.ReserveMapperFactory.GetMapper();
        }

        [HttpGet]
        public ActionResult Index()
        {
            IEnumerable<IPreReserve> preReserves = _cartManager.GetAll();
            IEnumerable<CartIndexModel> cart = GetCartIndexModel(preReserves);

            if (Request.IsAjaxRequest())
                return Json(cart, JsonRequestBehavior.AllowGet);

            return View(cart);
        }

        [HttpPost]
        public ActionResult RemovePreReserve(int session)
        {
            _cartManager.RemovePreReserve(session);

            if (Request.IsAjaxRequest())
                return Content("Pre reserva removida com sucesso!");

            return RedirectToAction("Index");
        }

        [Authorize(Roles = "client")]
        public ActionResult Reserve()
        {
            List<CartReserveModel> list = new List<CartReserveModel>();

            IEnumerable<IPreReserve> preReserves = _cartManager.GetAll();
            foreach (IPreReserve preReserve in preReserves)
            {
                ISession session = _sessionMapper.Get(preReserve.Session);
                CartReserveModel cartReserveModel = new CartReserveModel
                                                        {
                                                            SessionName = session.Name,
                                                            ShowName = session.Show.Name,
                                                            Reserved = _sessionMapper.LockSeats(session.Id, preReserve.Seats)
                                                        };

                if (cartReserveModel.Reserved)
                {
                    Reserve reserve = new Reserve(preReserve.Seats.ToArray(), User.Identity.Name, session);
                    _reserveMapper.Add(reserve);
                    _cartManager.RemovePreReserve(preReserve.Session);
                }

                list.Add(cartReserveModel);
            }

            if (list.Count == 0)
                return RedirectToAction("Index");

            return View(list);
        }

        public ActionResult CartBasket()
        {
            IEnumerable<CartIndexModel> cartIndexModels = GetCartIndexModel(_cartManager.GetAll());

            return PartialView("CartBasketControl", cartIndexModels);
        }

        #region Refactor Methods

        private IEnumerable<CartIndexModel> GetCartIndexModel(IEnumerable<IPreReserve> preReserves)
        {
            IEnumerable<CartIndexModel> cart = (from preReserve in preReserves
                                                let session = _sessionMapper.Get(preReserve.Session)
                                                select new CartIndexModel
                                                           {
                                                               PreReserveSeats = preReserve.Seats.ToArray(),
                                                               SessionId = session.Id,
                                                               SessionName = session.Name,
                                                               ShowName = session.Show.Name,
                                                               TicketPrice = session.TicketPrice
                                                           });
            return cart;
        }

        #endregion

        #region Ajax Actions

        [HttpPost]//    impossibilita que se façam gets a este método e seja retornado server error devido á segurança no Json, AllowGet
        public ActionResult AddSeat(int session, int seat)
        {
            if (!Request.IsAjaxRequest())
                return View("AjaxError");

            IPreReserve preReserve;
            bool mustUpdate = _cartManager.AddSeat(session, seat, out preReserve);
            if (!mustUpdate)
                return Json(new {MustUpdate = false});

            ISession sessionObj = _sessionMapper.Get(preReserve.Session);
            return Json(new
                            {
                                MustUpdate = true,
                                AddNode = preReserve.Seats.Count == 1,
                                NodeText = sessionObj.Show.Name + ", " + sessionObj.Name + ", " + preReserve.Seats.Count + " lugares;"
                            });
        }

        [HttpPost]//    impossibilita que se façam gets a este método e seja retornado server error devido á segurança no Json, AllowGet
        public ActionResult RemoveSeat(int session, int seat)
        {
            if (!Request.IsAjaxRequest())
                return View("AjaxError");

            IPreReserve preReserve;
            bool mustUpdate = _cartManager.RemoveSeat(session, seat, out preReserve);
            if (!mustUpdate)
                return Json(new {MustUpdate = false});

            if (preReserve.Seats.Count == 0)
            {
                return Json(new
                                {
                                    MustUpdate = true,
                                    RemoveNode = true
                                });
            }

            ISession sessionObj = _sessionMapper.Get(preReserve.Session);
            return Json(new
                            {
                                MustUpdate = true,
                                RemoveNode = false,
                                NodeText = sessionObj.Show.Name + ", " + sessionObj.Name + ", " + preReserve.Seats.Count + " lugares;"
                            });
        }

        #endregion

        #region Cart Manager class

        private class CartManager
        {
            private const string PreReserveSessionIdx = "PreReserves";

            private readonly List<IPreReserve> _preReservesEmptyList = new List<IPreReserve>();
            private readonly object _lock = new object();
            private readonly Controller _controller;


            public CartManager(Controller controller)
            {
                _controller = controller;
            }

            public IEnumerable<IPreReserve> GetAll()
            {
                lock (_lock)
                {
                    object obj = _controller.Session[PreReserveSessionIdx];
                    return obj == null ? _preReservesEmptyList : ((IDictionary<int, IPreReserve>)obj).Values.ToList();
                }
            }

            public bool AddSeat(int session, int seat, out IPreReserve preReserve)
            {
                lock (_lock)
                {
                    object obj = _controller.Session[PreReserveSessionIdx];

                    IDictionary<int, IPreReserve> preReserves;
                    if (obj == null)
                    {
                        //  atrasa a criação para quando for necessário
                        preReserves = new Dictionary<int, IPreReserve>();
                        _controller.Session[PreReserveSessionIdx] = preReserves;
                    }
                    else
                        preReserves = (IDictionary<int, IPreReserve>)obj;

                    if (!preReserves.TryGetValue(session, out preReserve))
                    {
                        preReserve = new PreReserve(session);
                        preReserves[session] = preReserve;
                    }

                    bool toAdd = !preReserve.Seats.Contains(seat);
                    if (toAdd)
                        preReserve.Seats.Add(seat);

                    return toAdd;
                }
            }

            public bool RemoveSeat(int session, int seat, out IPreReserve preReserve)
            {
                lock (_lock)
                {
                    preReserve = null;

                    object obj = _controller.Session[PreReserveSessionIdx];
                    if (obj == null)
                        return false;

                    IDictionary<int, IPreReserve> preReserves = (IDictionary<int, IPreReserve>)obj;
                    if (!preReserves.TryGetValue(session, out preReserve))
                        return false;

                    preReserve.Seats.Remove(seat);
                    if (preReserve.Seats.Count == 0)
                    {
                        preReserves.Remove(session);
                    }

                    return true;
                }
            }

            public bool RemovePreReserve(int session)
            {
                lock (_lock)
                {
                    object obj = _controller.Session[PreReserveSessionIdx];
                    if (obj == null)
                        return false;
                    IDictionary<int, IPreReserve> preReserves = (IDictionary<int, IPreReserve>)obj;

                    return preReserves.Remove(session);
                }
            }
        }

        #endregion
    }
}
