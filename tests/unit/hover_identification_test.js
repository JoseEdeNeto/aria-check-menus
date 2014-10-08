(function () {
    var fixtures_url = casper.cli.get("fixtures");
    casper.echo(fixtures_url);
    casper.test.begin("OnMouseOver events set to HTML can be counted for each element", 1, function (test) {
        casper.options.clientScripts = ["lib/OnMouseOverObserver.js"];
        casper.start(fixtures_url + "hover_elements01.html", function () {
            var self = this,
                onmouseover_elements = self.evaluate(function () {
                    return window.OnMouseOverObserver.size();
                });
            test.assertEquals(onmouseover_elements, 3);
        });
        casper.run(function () {
            test.done();
        });
    });
    casper.test.begin("OnMouseOver events set to HTML can be counted for each element again", 1, function (test) {
        casper.options.clientScripts = ["lib/OnMouseOverObserver.js"];
        casper.start(fixtures_url + "hover_elements02.html", function () {
            var self = this,
                onmouseover_elements = self.evaluate(function () {
                    return window.OnMouseOverObserver.size();
                });
            test.assertEquals(onmouseover_elements, 2);
        });
        casper.run(function () {
            test.done();
        });
    });
}());
