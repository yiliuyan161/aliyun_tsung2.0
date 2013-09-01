App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.Router.map(function() {
    this.route("password",{ path: "/password" });
    this.route("env",{ path: "/env" });
    this.route("ssh",{ path: "/ssh" });
    this.route("server",{ path: "/server" });
    // put your routes here
});

App.IndexRoute = Ember.Route.extend({
    model: function() {
        return ['redi', 'yellow', 'blue'];
    }
});
App.PasswordRoute = Ember.Route.extend({
    model: function() {
        return ['redp', 'yellow', 'blue'];
    }
});
App.EnvRoute = Ember.Route.extend({
    model: function() {
        return ['rede', 'yellow', 'blue'];
    }
});
App.SshRoute = Ember.Route.extend({
    model: function() {
        return ['reds', 'yellow', 'blue'];
    }
});
App.ServerRoute = Ember.Route.extend({
    model: function() {
        return ['redse', 'yellow', 'blue'];
    }
});