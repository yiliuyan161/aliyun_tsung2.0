App = {};
App.indexModel = {
    show: ko.observable(true)



};
App.passwordModel={
    show: ko.observable(false)
}
App.envModel={
    show: ko.observable(false)
}
App.sshModel={
    show: ko.observable(false)
}
App.serverModel={
    show: ko.observable(false)
}



ko.applyBindings(App.indexModel,$("#index")[0]);
ko.applyBindings(App.passwordModel,$("#pass")[0]);
ko.applyBindings(App.envModel,$("#env")[0]);
ko.applyBindings(App.serverModel,$("#server")[0]);
ko.applyBindings(App.sshModel,$("#ssh")[0]);

