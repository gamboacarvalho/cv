using System.ComponentModel.DataAnnotations;

namespace TicketsOnline.Models
{
    #region Account Form Models

    public class RegisterFormModel
    {
        //  retirado de http://www.codeproject.com/KB/recipes/EmailRegexValidator.aspx
        internal const string EmailValidationString = @"^(([\w-]+\.)+[\w-]+|([a-zA-Z]{1}|[\w-]{2,}))@"
                                                     +
                                                     @"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\.([0-1]?
				[0-9]{1,2}|25[0-5]|2[0-4][0-9])\."
                                                     +
                                                     @"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\.([0-1]?
				[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                                                     + @"([a-zA-Z]+[\w-]+\.)+[a-zA-Z]{2,4})$";

        [Required(ErrorMessage = "É necessário inserir o username.")]
        public string Username { get; set; }

        [Required(ErrorMessage = "É necessário inserir o email.")]
        [RegularExpression(EmailValidationString, ErrorMessage = "O email inserido é inválido!")]
        public string Email { get; set; }

        [Required(ErrorMessage = "É necessário inserir o nome.")]
        public string Name { get; set; }

        [Required(ErrorMessage = "É necessário inserir uma password.")]
        [StringLength(200, MinimumLength = 5, ErrorMessage = "A password deve ter entre 5 e 200 caracteres.")]
        public string Password { get; set; }

        [Required(ErrorMessage = "É necessário inserir password de validação.")]
        [StringLength(200, MinimumLength = 5, ErrorMessage = "A password deve ter entre 5 e 200 caracteres.")]
        public string ValidatePassword { get; set; }
    }

    public class LogOnFormModel
    {
        [Required(ErrorMessage = "É necessário inserir o username.")]
        public string Username { get; set; }

        [Required(ErrorMessage = "É necessário inserir uma password.")]
        [StringLength(200, MinimumLength = 5, ErrorMessage = "A password deve ter entre 5 e 200 caracteres.")]
        public string Password { get; set; }
    }

    public class AjaxLogOnFormModel:LogOnFormModel
    {
        public string Data { get; set; }
        public string Url { get; set; }
    }

    public class ManageFormModel
    {
        [Required(ErrorMessage = "É necessário inserir o email.")]
        [RegularExpression(RegisterFormModel.EmailValidationString, ErrorMessage = "O email inserido é inválido!")]
        public string Email { get; set; }

        [Required(ErrorMessage = "É necessário inserir o nome.")]
        public string Name { get; set; }

        public bool HasImage { get; set; }

        [Required(ErrorMessage = "É necessário inserir a password anterior para confirmar.")]
        [StringLength(200, MinimumLength = 5, ErrorMessage = "A password deve ter entre 5 e 200 caracteres.")]
        public string Password { get; set; }

        [StringLength(200, MinimumLength = 5, ErrorMessage = "A password deve ter entre 5 e 200 caracteres.")]
        public string NewPassword { get; set; }

        [StringLength(200, MinimumLength = 5, ErrorMessage = "A password deve ter entre 5 e 200 caracteres.")]
        public string ValidatePassword { get; set; }
    }

    #endregion

    #region Comment Form Model

    public class CommentFormModel
    {
        [Required(ErrorMessage = "Necessita de inserir titulo")]
        public string Title { get; set; }
        [Required(ErrorMessage = "Tem de inserir um comentário")]
        public string Comment { get; set; }
        public int ShowId { get; set; }
        public string Status { get; set; }

        public CommentFormModel()
        {
            Status = "";
        }
    }

    #endregion
}