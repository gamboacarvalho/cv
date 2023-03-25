using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;
using Repository;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;
using TicketsOnline.Models;

namespace TicketsOnline.Controllers
{
    [Authorize(Roles = "admin")]
    public class AdminController : Controller
    {
        private readonly IUserMapper _userMapper = DataRepository.UserMapperFactory.GetMapper();
        private readonly IReserveMapper _reserveMapper = DataRepository.ReserveMapperFactory.GetMapper();
        private readonly ISessionMapper _sessionMapper = DataRepository.SessionMapperFactory.GetMapper();

        [HttpGet]
        public ActionResult Index()
        {
            AdminModel adminModel = new AdminModel();

            foreach (IUser user in _userMapper.GetAll())
            {
                if (user.Roles.Any(s => s.Equals("admin")))
                    adminModel.Admins.Add(user);
                else
                    adminModel.Clients.Add(user);
            }

            return View(adminModel);
        }

        [HttpPost]
        public ActionResult Delete(string id)
        {
            string ret;
            try
            {
                IUser user = _userMapper.Get(id);
                if (user.Roles.Any(r => r.Equals("admin")))
                {
                    ret = "Não pode apagar administradores remotamente!";
                    ViewData["error"] = ret;
                }  
                else
                {
                    foreach (IReserve reserve in _reserveMapper.GetReservesByUser(user.UserName))
                    {
                        _sessionMapper.UnlockSeats(reserve.Session.Id, reserve.Seats);
                        _reserveMapper.Remove(reserve.Id);
                    }
                    _userMapper.Remove(id);

                    ret = "Cliente " + user.Name + " (username: " + user.UserName + ") removido com sucesso!";
                    ViewData["error"] = ret;
                }
            }
            catch (Exception)
            {
                ret = "Ocorreu um erro durante o pedido de remoção! Verifique a integridade da base de dados!";
                ViewData["error"] = ret;
            }

            if (Request.IsAjaxRequest())
                return Content(ret);

            return RedirectToAction("Index");
        }
    }
}
