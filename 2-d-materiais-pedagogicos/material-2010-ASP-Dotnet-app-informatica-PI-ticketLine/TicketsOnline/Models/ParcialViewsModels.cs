using System.Collections.Generic;
using RepositoryInterfaces.DataObjects;

namespace TicketsOnline.Models
{
    public class MenuButtonControlModel
    {
        public string Action { get; set; }
        public string Controller { get; set; }
        public string ButtonValue { get; set; }
    }

    public class ShowsSelectorControlModel
    {
        public string Title { get; set; }
        public string SelectorId { get; set; }
        public string Action { get; set; }
        public string Controller { get; set; }
        public string DomIdToUpdate { get; set; }
        public string OnChangeEventExtraCode { get; set; }
        public IEnumerable<SelectorOptionModel> SelectorOptions { get; set; }

        public ShowsSelectorControlModel(){}

        public ShowsSelectorControlModel(string title, string selectorId, string action, string controller, string domIdToUpdate, string onChangeEvent, IEnumerable<SelectorOptionModel> selectorOptions)
        {
            Title = title;
            SelectorId = selectorId;
            Action = action;
            Controller = controller;
            DomIdToUpdate = domIdToUpdate;
            OnChangeEventExtraCode = onChangeEvent;
            SelectorOptions = selectorOptions;
        }
    }

    public class SelectorOptionModel
    {
        public int Id { get; set; }
        public string Text { get; set; }
    }

    public class StarRatingControlModel
    {
        public const int StarCount = 5;

        public int Rating { get; set; }
        public int TotalVotes { get; set; }
        public string Text { get; set; }

        public StarRatingControlModel()
        {
            Text = "";
        }
    }

    public class SessionSelectOptionModel
    {
        public ShowsSelectorControlModel ShowsSelectorControlModel { get; set; }
        public string ShowName { get; set; }
        public int ShowId { get; set; }
        public StarRatingControlModel StarRatingControlModel { get; set; }
    }
}