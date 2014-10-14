(function () {
    var fixtures_url = casper.cli.get("fixtures");
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
    casper.test.begin("OnMouseOver events set to HTML should be returned orderly", 3, function (test) {
        casper.options.clientScripts = ["lib/OnMouseOverObserver.js"];
        casper.start(fixtures_url + "hover_elements01.html", function () {
            var self = this,
                onmouseover_elements = self.evaluate(function () {
                    return [window.OnMouseOverObserver.popActivator().textContent,
                            window.OnMouseOverObserver.popActivator().textContent,
                            window.OnMouseOverObserver.popActivator().textContent];
                });
            test.assertEquals(onmouseover_elements[0], "on3");
            test.assertEquals(onmouseover_elements[1], "on2");
            test.assertEquals(onmouseover_elements[2], "on1");
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
