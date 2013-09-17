function clearPanel(){
    // You can put some code in here to do fancy DOM transitions, such as fade-out or slide-in.
}

Path.map("#/index").to(function(){
    App.indexModel.show(true);
    App.envModel.show(false);
    App.sshModel.show(false);
    App.passwordModel.show(false);
    App.serverModel.show(false);
});

Path.map("#/password").to(function(){
    App.indexModel.show(false);
    App.envModel.show(false);
    App.sshModel.show(false);
    App.passwordModel.show(true);
    App.serverModel.show(false);
}).enter(clearPanel);

Path.map("#/env").to(function(){
    App.indexModel.show(false);
    App.envModel.show(true);
    App.sshModel.show(false);
    App.passwordModel.show(false);
    App.serverModel.show(false);
}).enter(clearPanel);

Path.map("#/ssh").to(function(){
    App.indexModel.show(false);
    App.envModel.show(false);
    App.sshModel.show(true);
    App.passwordModel.show(false);
    App.serverModel.show(false);
}).enter(clearPanel);

Path.map("#/server").to(function(){
    App.indexModel.show(false);
    App.envModel.show(false);
    App.sshModel.show(false);
    App.passwordModel.show(false);
    App.serverModel.show(true);
}).enter(clearPanel);

Path.root("#/index");

Path.listen();