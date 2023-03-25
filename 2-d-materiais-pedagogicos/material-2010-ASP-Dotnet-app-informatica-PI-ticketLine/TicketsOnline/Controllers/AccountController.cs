using System;
using System.Web;
using System.Web.Mvc;
using Repository;
using Repository.DataObjets;
using RepositoryInterfaces.DataObjects;
using RepositoryInterfaces.Mappers;
using TicketsOnline.Models;

namespace TicketsOnline.Controllers
{
    public class AccountController : Controller
    {
        private readonly IUserMapper _userMapper = DataRepository.UserMapperFactory.GetMapper();
        private readonly ILoginMapper _loginMapper = DataRepository.LoginMapperFactory.GetMapper();

        [HttpGet]
        public ActionResult Index()
        {
            return RedirectToAction("LogOn");
        }

        [HttpGet]
        public FileContentResult GetImage(string id)
        {
            IUser user = _userMapper.Get(id);

            return File(user.ImageData, user.ImageMimeType);
        }


        #region Account/Register

        [HttpGet]
        public ActionResult Register()
        {
            RegisterFormModel registerFormModel = new RegisterFormModel();
            return View(registerFormModel);
        }

        [HttpPost]
        public ActionResult Register(RegisterFormModel formModel, HttpPostedFileBase image)
        {
            if (!ModelState.IsValid)
                return View(formModel);

            if (!formModel.Password.Equals(formModel.ValidatePassword))
            {
                ViewData["error"] = "As passwords não são identicas. Tente novamente!";
                return View(formModel);
            }

            try
            {
                IUser user = new Client(formModel.Username, formModel.Password, formModel.Email, formModel.Name);

                if (image != null)
                {
                    user.ImageMimeType = image.ContentType;
                    user.ImageData = new byte[image.ContentLength];
                    image.InputStream.Read(user.ImageData, 0, image.ContentLength);
                }

                _userMapper.Add(user);
                return RedirectToAction("LogOn");
            }
            catch (InvalidOperationException)
            {
                ViewData["error"] = "Um utilizador com esse username já existe. Escolha um novo.";
                return View(formModel);
            }
        }

        #endregion

        #region Account/LogOn

        [HttpGet]
        public ActionResult LogOn()
        {
            LogOnFormModel logOnFormModel = new LogOnFormModel();
            return View(logOnFormModel);
        }

        [HttpPost]
        public ActionResult LogOn(LogOnFormModel formModel)
        {
            if (!ModelState.IsValid)
            {
                if (Request.IsAjaxRequest())
                {
                    return Json(new
                                    {
                                        Sucess = false,
                                        Error = "Verifique o preenchimento e validade dos campos!"
                                    });
                }

                return View(formModel);
            }


            try
            {
                if (_userMapper.IsValid(formModel.Username, formModel.Password))
                {
                    int id = _loginMapper.Add(new Login(formModel.Username, DateTime.Now.AddMinutes(30.0)));

                    HttpCookie cookie = Request.Cookies.Get("login_cookie") ?? new HttpCookie("login_cookie");
                    cookie["sessionId"] = id.ToString();

                    Response.Cookies.Add(cookie);

                    if (Request.IsAjaxRequest())
                        return Json(new
                                        {
                                            Sucess = true,
                                            _userMapper.Get(formModel.Username).Name
                                        });

                    string url = Request.QueryString["ReturnUrl"];
                    if (url != null)
                        return Redirect(url);

                    return RedirectToAction("Index", "Home");
                }
            }
            catch
            {

            }

            if (Request.IsAjaxRequest())
                return Json(new
                                {
                                    Sucess = false,
                                    Error = "Credenciais inválidas"
                                });

            ViewData["error"] = "Credenciais inválidas! Insira novamente.";
            return View(formModel);
        }

        #endregion

        #region Account/LogOff

        public ActionResult LogOff()
        {
            HttpCookie cookie = Request.Cookies.Get("login_cookie");
            if (cookie == null)
                return RedirectToAction("Index", "Home");

            int id = int.Parse(cookie["sessionId"]);
            _loginMapper.Remove(id);

            cookie = new HttpCookie("login_cookie") { Expires = DateTime.Now.AddDays(-1d) };
            Response.Cookies.Add(cookie);

            return RedirectToAction("Index", "Home");
        }

        #endregion

        #region Account/Manage

        [HttpGet]
        [Authorize]
        public ActionResult Manage()
        {
            try
            {
                IUser user = _userMapper.Get(User.Identity.Name);

                ManageFormModel formModel = new ManageFormModel
                                                {
                                                    Email = user.Email,
                                                    Name = user.Name,
                                                    HasImage = user.ImageData != null
                                                };
                return View(formModel);
            }
            catch (Exception)
            {
                return RedirectToAction("Index", "Home");
            }
        }

        [HttpPost]
        [Authorize]
        public ActionResult Manage(ManageFormModel formModel, HttpPostedFileBase image)
        {
            if (!ModelState.IsValid)
                return View(formModel);

            if (formModel.NewPassword != null && formModel.ValidatePassword != null)
            {
                if (!formModel.NewPassword.Equals(formModel.ValidatePassword))
                {
                    ViewData["error"] = "A nova password e a de validação não são identicas! Tente novamente.";
                    return View(formModel);
                }
            }

            if (!_userMapper.IsValid(User.Identity.Name, formModel.Password))
            {
                ViewData["error"] = "A password antiga está incorrecta! Tente novamente.";
                formModel.HasImage = _userMapper.Get(User.Identity.Name).ImageData != null;
                return View(formModel);
            }

            Client client = formModel.NewPassword != null ?
                new Client(formModel.NewPassword) { Email = formModel.Email, Name = formModel.Name } :
                new Client(formModel.Password) { Email = formModel.Email, Name = formModel.Name };

            if (image != null)
            {
                client.ImageMimeType = image.ContentType;
                client.ImageData = new byte[image.ContentLength];
                image.InputStream.Read(client.ImageData, 0, image.ContentLength);
                formModel.HasImage = true;
            }
            else
                formModel.HasImage = _userMapper.Get(User.Identity.Name).ImageData != null;

            if (_userMapper.Update(User.Identity.Name, client))
                ViewData["error"] = "Alterações efectuadas com sucesso!";
            else
                ViewData["error"] = "Não foi possivel efectuar as alterações! Tente novamente.";

            return View(formModel);
        }

        #endregion
    }
}
