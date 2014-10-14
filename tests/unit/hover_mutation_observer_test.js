(function () {
    var fixture_url = casper.cli.get("fixtures");
    casper.options.clientScripts = [
        "lib/mutationrecorder/OverMutationRecorder.js",
        "lib/mutationrecorder/ClassNameVerifier.js",
        "lib/mutationrecorder/InnerHTMLVerifier.js",
        "lib/mutationrecorder/CSSStyleVerifier.js"
    ];
    casper.test.begin("OverMutationRecorder should record CSS change mutations in target", 1, function (test) {
        casper.start(fixture_url + "mouseover_mutation_observer01.html", function () {
            var self = this,
                result = "";
            self.evaluate(function () {
                window.recorder = OverMutationRecorder([ClassNameVerifier, CSSStyleVerifier]);
            });
            self.mouseEvent("mouseover", "#link2");
            result = self.evaluate(function () {
                return recorder.popLastEvent().target.textContent.trim();
            });
            test.assertEquals(result, "Useful message 2");
        });
        casper.run(function () {
            test.done();
        });
    });

    casper.test.begin("OverMutationRecorder should record CSS change mutations in multiple targets", 1, function (test) {
        casper.start(fixture_url + "mouseover_mutation_observer02.html", function () {
            var self = this,
                result = "";
            self.evaluate(function () {
                window.recorder = OverMutationRecorder([ClassNameVerifier, CSSStyleVerifier]);
            });
            self.mouseEvent("mouseover", "#link2");
            result = self.evaluate(function () {
                return recorder.popLastEvent().target.textContent.trim();
            });
            test.assertEquals(result, "Useful message33");
        });
        casper.run(function () {
            test.done();
        });
    });

    casper.test.begin("OverMutationRecorder should record className mutations in targets", 1, function (test) {
        casper.start(fixture_url + "mouseover_mutation_observer03.html", function () {
            var self = this,
                result = "";
            self.evaluate(function () {
                window.recorder = OverMutationRecorder([ClassNameVerifier, CSSStyleVerifier]);
            });
            self.mouseEvent("mouseover", "#link2");
            result = self.evaluate(function () {
                return recorder.popLastEvent().target.textContent.trim();
            });
            test.assertEquals(result, "Useful message for className changes");
        });
        casper.run(function () {
            test.done();
        });
    });

    casper.test.begin("OverMutationRecorder should record innerHTML mutations in targets", 2, function (test) {
        casper.start(fixture_url + "mouseover_mutation_observer04.html", function () {
            var self = this,
                result = "";
            self.evaluate(function () {
                window.recorder = OverMutationRecorder([InnerHTMLVerifier]);
            });
            self.mouseEvent("mouseover", "#link2");
            result = self.evaluate(function () {
                var target = recorder.popLastEvent().target;
                return [target.tagName, target.innerHTML];
            });
            test.assertEquals(result[0], "DIV");
            test.assertEquals(result[1], "<span>abobrinha</span>");
        });
        casper.run(function () {
            test.done();
        });
    });
}());
