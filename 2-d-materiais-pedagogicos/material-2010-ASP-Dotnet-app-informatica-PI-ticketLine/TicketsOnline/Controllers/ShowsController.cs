using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Web.Mvc;
using Repository;
using Repository.DataObjets;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;
using TicketsOnline.Models;

namespace TicketsOnline.Controllers
{
    public class ShowsController : Controller
    {
        private readonly IShowMapper _showMapper = DataRepository.ShowMapperFactory.GetMapper();
        private readonly ISessionMapper _sessionMapper = DataRepository.SessionMapperFactory.GetMapper();
        private readonly ICommentMapper _commentMapper = DataRepository.CommentMapperFactory.GetMapper();
        private readonly IUserMapper _userMapper = DataRepository.UserMapperFactory.GetMapper();

        [HttpGet]
        public ActionResult Index()
        {
            return View(new ShowsSelectorControlModel
                            {
                                Title = "Escolha o espectáculo: ",
                                SelectorId = "showSelect",
                                Action = "SessionByShow",
                                Controller = "Shows",
                                DomIdToUpdate = "session",
                                SelectorOptions =
                                    _showMapper.GetAll().Select(
                                        show => new SelectorOptionModel
                                                    {
                                                        Id = show.Id,
                                                        Text = show.Name
                                                    }),
                                OnChangeEventExtraCode = "cleanSessionAndRoom();"
                            });
        }

        [HttpGet]
        public ActionResult SessionByShow(int id)
        {
            if (!Request.IsAjaxRequest())
                return View("AjaxError");

            //Thread.Sleep(3000);

            IShow show = _showMapper.Get(id);

            return PartialView("SessionSelectOptionControl",
                               new SessionSelectOptionModel
                                   {
                                       ShowId = id,
                                       ShowName = show.Name,
                                       StarRatingControlModel = new StarRatingControlModel
                                                                    {
                                                                        Rating = show.Rating,
                                                                        TotalVotes = show.Ratings.Count
                                                                    },
                                       ShowsSelectorControlModel = new ShowsSelectorControlModel
                                                                       {
                                                                           Title = "Escolha a sessão: ",
                                                                           SelectorId = "sessionSelect",
                                                                           Action = "Room",
                                                                           Controller = "Shows",
                                                                           DomIdToUpdate = "room",
                                                                           SelectorOptions =
                                                                               _sessionMapper.GetAll().Where(
                                                                                   session => session.Show.Id == id).
                                                                               Select(
                                                                                   session => new SelectorOptionModel
                                                                                                  {
                                                                                                      Id = session.Id,
                                                                                                      Text =
                                                                                                          session.Name
                                                                                                  }),
                                                                           OnChangeEventExtraCode = "cleanRoom();"
                                                                       }
                                   }
                );
        }

        [HttpGet]
        public ActionResult Room(int id)
        {
            if (!Request.IsAjaxRequest())
                return View("AjaxError");

            //Thread.Sleep(3000);

            ISession session = _sessionMapper.Get(id);

            return PartialView("RoomControl", session);
        }

        [HttpGet]
        public ActionResult GetCommentsInterface(int showId)
        {
            return PartialView("GetCommentsInterfaceControl", showId);
        }

        [HttpPost]
        public ActionResult Comment(CommentFormModel model)
        {
            if (!ModelState.IsValid)
            {
                if (Request.IsAjaxRequest())
                    return PartialView("UserCommentControl", model);

                ViewData["error"] = "Precisa de preencher todos os campos no seu comentário!";
                return RedirectToAction("Index", "Shows");
            }
            string userName = User.Identity.IsAuthenticated ? _userMapper.Get(User.Identity.Name).Name : "Anónimo";

            IComment comment = new Comment(model.ShowId, userName, model.Title, model.Comment, DateTime.Now);

            _commentMapper.Add(comment);

            return PartialView("UserCommentControl", new CommentFormModel { ShowId = model.ShowId, Status = "Comentário inserido com sucesso! Actualize os comentários para visualizar" });
        }

        [HttpGet]
        public ActionResult GetAllComments(int id)
        {
            IEnumerable<IComment> comments = _commentMapper.GetByShow(id);

            return PartialView("GetAllCommentsControl", comments);
        }

        [HttpPost]
        public ActionResult RateMovie(int showId, int rating)
        {
            if (!User.Identity.IsAuthenticated || !User.IsInRole("client"))
                //return Content("<b>Precisa de iniciar sessão para votar!</b>", "text/html");
                return PartialView("LogOnPopupControl", new LogOnFormModel());

            if (rating < 0 || rating > StarRatingControlModel.StarCount)
                return Content("<b>Votação com valores inválidos</b>", "text/html");

            return Content(_showMapper.RateMovie(showId, new UserRating(User.Identity.Name, rating)) ? "<b>Votação inserida com sucesso! Obrigado pela sua opinião!</b>" : "<b>Apenas pode votar uma vez por espectáculo!</b>", "text/html");
        }
    }
}
