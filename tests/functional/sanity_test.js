(function () {
    var fixtures_dir = casper.cli.get("fixtures");
    casper.options.clientScripts = ["lib/Utils.js"];
    casper.test.begin("sanity test for functional environment with fixture", 2, function (test) {
        casper.start(fixtures_dir + "sanity_check01.html", function () {
            var self = this, tooltip_id;
            test.assertTitle("Index");
            tooltip_id = self.evaluate(function () {
                return document.querySelectorAll("body > a:nth-child(3)")[0].id;
            });
            test.assertEquals(tooltip_id, "link2");
        });
        casper.run(function () {
            test.done();
        });
    });

    casper.test.begin("SelectorFinder whether the selector actually resolves inside PhantomJS", 1, function (test) {
        casper.start(fixtures_dir + "selector_finder02.html", function () {
            var self = this,
                selector = self.evaluate(function () {
                    return Utils.getSelector(document.querySelector("#procurar4"));
                });
            self.click(selector);
            test.assertEquals(self.evaluate(function () {
                return document.querySelector("#new_element").textContent;
            }), "Abobrinha mor");
        });
        casper.run(function () {
            test.done();
        });
    });
}());
