using System.Collections.Generic;
using System.Web.Mvc;
using Repository;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;

namespace TicketsOnline.Controllers
{
    [Authorize(Roles = "client")]
    public class ReservesController : Controller
    {

        private readonly IReserveMapper _reserveMapper = DataRepository.ReserveMapperFactory.GetMapper();
        private readonly ISessionMapper _sessionMapper = DataRepository.SessionMapperFactory.GetMapper();

        [HttpGet]
        public ActionResult Index()
        {
            string name = User.Identity.Name;

            IEnumerable<IReserve> reserves = _reserveMapper.GetReservesByUser(name);

            return View(reserves);
        }

        [HttpPost]
        public ActionResult Delete(int id)
        {
            IReserve reserve = _reserveMapper.Get(id);
            _sessionMapper.UnlockSeats(reserve.Session.Id, reserve.Seats);
            _reserveMapper.Remove(id);

            if (Request.IsAjaxRequest())
                return Content("Reserva removida com sucesso!");

            return RedirectToAction("Index");
        }
    }
}
