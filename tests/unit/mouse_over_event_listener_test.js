(function () {
    var fixtures_url = casper.cli.get("fixtures");
    casper.test.begin("MouseOverEventListener set to HTML can be counted for each element", 1, function (test) {
        casper.options.clientScripts = [];
        casper.options.onPageInitialized = function (page) {
            page.injectJs("lib/MouseOverEventListenerObserver.js");
        };
        casper.start(fixtures_url + "hover_elements01.html", function () {
            var self = this,
                onmouseover_elements = self.evaluate(function () {
                    return window.MouseOverEventListenerObserver.size();
                });
            test.assertEquals(onmouseover_elements, 3);
        });
        casper.run(function () {
            test.done();
        });
    });
    casper.test.begin("MouseOverEventListener set to HTML should be returned in order", 3, function (test) {
        casper.options.clientScripts = [];
        casper.options.onPageInitialized = function (page) {
            page.injectJs("lib/MouseOverEventListenerObserver.js");
        };
        casper.start(fixtures_url + "hover_elements01.html", function () {
            var self = this,
                onmouseover_elements = self.evaluate(function () {
                    return [MouseOverEventListenerObserver.popActivator().textContent,
                            MouseOverEventListenerObserver.popActivator().textContent,
                            MouseOverEventListenerObserver.popActivator().textContent];
                });
            test.assertEquals(onmouseover_elements[0], "addEventListener1");
            test.assertEquals(onmouseover_elements[1], "addEventListener3");
            test.assertEquals(onmouseover_elements[2], "addEventListener2");
        });
        casper.run(function () {
            test.done();
        });
    });
    casper.test.begin("MouseOverEventListener set to HTML can be counted for each element (again)", 1, function (test) {
        casper.options.clientScripts = [];
        casper.options.onPageInitialized = function (page) {
            page.injectJs("lib/MouseOverEventListenerObserver.js");
        };
        casper.start(fixtures_url + "hover_elements03.html", function () {
            var self = this,
                onmouseover_elements = self.evaluate(function () {
                    return window.MouseOverEventListenerObserver.size();
                });
            test.assertEquals(onmouseover_elements, 2);
        });
        casper.run(function () {
            test.done();
        });
    });
}());
