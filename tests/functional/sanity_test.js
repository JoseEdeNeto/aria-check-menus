(function () {
    var fixtures_dir = casper.cli.get("fixtures");
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
}());
