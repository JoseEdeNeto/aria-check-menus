var app = require("App"),
    casper = require("casper"),
    params;

params = app.init({casper: casper, wait: 500});
app.captureWidgets(params.cli.get(0), "captured_widgets/");
