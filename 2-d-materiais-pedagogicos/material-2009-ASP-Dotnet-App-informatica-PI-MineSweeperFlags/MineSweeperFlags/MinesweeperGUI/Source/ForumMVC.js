// Forum MVC initializer


var Forum = new Object();

Forum.init = function(pName, eMail) {

    ForumModel.init(pName, eMail);
    ForumView.init();
    ForumController.init();
    ForumController.evtListThreads();
}


// Forum Model ---------------------------------------------------------------------------------------

var ForumModel = new Object();
ForumModel.init = function(pName, eMail) {

    var _pName = pName;
    var _eMail = eMail;
	var _currThread = 0;

    this.setCurrThread = function(thId) { _currThread = thId; }
    this.getCurrThread = function() { return _currThread; }

    this.setPlayerName = function(pName) { _pName = pName; }
    this.getPlayerName = function() { return _pName; }

    this.setPlayerEMail = function(eMail) { _eMail = eMail; }
    this.getPlayerEMail = function() { return _eMail; }
}


// Forum Controller ----------------------------------------------------------------------------------

var ForumController = new Object();
ForumController.init = function() {

    var handlerClass = "Forum";
    ForumView.renderNav();

	//ForumView.hideAddPostButton();

    // --------------------------------    
    // Events
	
    this.evtListThreads = function() {
        try {
            var req = new HttpRequest(handlerClass, "ListThreads", "gName", 0, "eMail", ForumModel.getPlayerEMail());
            req.Request();
			ForumView.renderContent(req.getResponseText());
        }
        catch (e) { alert(e); }
		ForumModel.setCurrThread(0);
		ForumView.showAddThreadButton();
		ForumView.hideAddPostButton();

    }

    this.evtListPosts = function(thId) {
        try {
            var req = new HttpRequest(handlerClass, "ListPosts", "gName", 0, "eMail", ForumModel.getPlayerEMail(), "thId", thId);
            req.Request();
			ForumView.renderContent(req.getResponseText());
        }
        catch (e) { alert(e); }
		ForumModel.setCurrThread(thId);
		ForumView.hideAddThreadButton();
		ForumView.showAddPostButton();
		
    }

	this.evtAddPost = function() { ForumView.renderAddPost(); }
	
	this.evtSavePost = function() {

		postBody = ForumView.getPostBody();
		
		if (postBody.length < 3){
			alert("Post body must be at least 3 characters long!");
			ForumView.setFocusThreadTitle();
		}
		else {
		
			try {
				var req = new HttpRequest(handlerClass, "AddPost", "gName", 0,
					"eMail", ForumModel.getPlayerEMail(),
					"body", postBody,
					"thId", ForumModel.getCurrThread());
				req.Request();
				var resp = req.getResponseText();
				if (resp == '')
					alert ("Unknown error occurred! Try again!");
				else {
					ForumView.hideAddPost();
					ForumView.renderContent(resp);
				}
			}
			catch (e) { alert(e); }
		}
	}
	
	this.evtAddThread = function() { ForumView.renderAddThread(); }
	
	this.evtSaveThread = function() {
	
		thTitle = ForumView.getThreadTitle();
		if (thTitle.length < 1){
			alert("Thread title must be at least 1 character long!");
			ForumView.setFocusThreadTitle();
		}
		else {
			try {
				var req = new HttpRequest(handlerClass, "AddThread", "gName", 0, "eMail", ForumModel.getPlayerEMail(), "thTitle", thTitle);
				req.Request();
				var resp = req.getResponseText();
				if (resp == thTitle)
					alert ("Thread name already exists!");
				else if (resp == '')
					alert ("Unknown error occurred! Try again!");
				else {
					ForumView.hideAddThread();
					ForumView.renderContent(resp);
				}
			}
			catch (e) { alert(e); }
		}
	}
}

// Forum View ----------------------------------------------------------------------------------------

var ForumView = new Object();
ForumView.init = function() {

    var encodeEmail = function(email) { return email.replace(".", "DOT_SYMBOL").replace("@", "AT_SYMBOL"); }
    var decodeEmail = function(email) { return email.replace("DOT_SYMBOL", ".").replace("AT_SYMBOL", "@"); }

    // --------------------------------
    // Navigation
    this.renderNav = function() {
        var navDiv = $("#forumNav");
		$("<button/>").click(function() { ForumController.evtListThreads(); }).attr("id","ThListButton").text("Thread List").appendTo(navDiv);
		$("<button/>").click(function() { ForumController.evtAddThread(); }).attr("id","ThAddButton").text("Add Thread").hide().appendTo(navDiv);
		$("<button/>").click(function() { ForumController.evtAddPost(); }).attr("id","PostAddButton").text("Add Post").hide().appendTo(navDiv);
		this.hideAddPostButton();
	}
	
	this.showAddPostButton = function() { $("#PostAddButton").show("slow"); }
	this.hideAddPostButton = function() { $("#PostAddButton").hide("slow"); }
	
	this.showAddThreadButton = function() { $("#ThAddButton").show("slow"); }
	this.hideAddThreadButton = function() { $("#ThAddButton").hide("slow"); }

	
    // --------------------------------
    // Content Div
    this.renderContent = function(html) { $("#forumContent").empty().html(html); }
	
	
	// --------------------------------
    // Thread Data Entry
    this.renderAddThread = function() {
		var addThDiv = $("#forumThreadEntry");
		addThDiv.empty();
		$("<input/>").attr({"id":"ThTitleInput","name":"ThTitleInput"}).text("").appendTo(addThDiv);
		$("<button/>").click(function() { ForumController.evtSaveThread(); }).attr("id","ThSaveButton").text("Save").appendTo(addThDiv);
		$("<button/>").click(function() { ForumView.hideAddThread(); }).attr("id","ThBackButton").text("Cancel").appendTo(addThDiv);
	}
	
	this.hideAddThread = function() { $("#forumThreadEntry").empty(); }
	
	this.getThreadTitle = function() { return $("#ThTitleInput").val(); }
	this.setFocusThreadTitle = function() { $("#ThTitleInput").focus(); }

	// --------------------------------
    // Post Data Entry
    this.renderAddPost = function() {
		var addPostDiv = $("#forumPostEntry");
		addPostDiv.empty();
		$("<textarea/>").attr({"id":"PostInput","name":"PostInput"}).text("").appendTo(addPostDiv);
		$("<button/>").click(function() { ForumController.evtSavePost(); }).attr("id","PostSaveButton").text("Save").appendTo(addPostDiv);
		$("<button/>").click(function() { ForumView.hideAddPost(); }).attr("id","PostBackButton").text("Cancel").appendTo(addPostDiv);
	}
	
	this.hideAddPost = function() { $("#forumPostEntry").empty(); }
	
	this.getPostBody = function() { return $("#PostInput").val(); }
	this.setFocusPostBody = function() { $("#PostInput").focus(); }
}
