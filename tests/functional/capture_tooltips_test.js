(function () {
    var fixtures_url = casper.cli.get("fixtures");
    casper.options.clientScripts = [
        "lib/mutationrecorder/OverMutationRecorder.js",
        "lib/mutationrecorder/ClassNameVerifier.js",
        "lib/mutationrecorder/InnerHTMLVerifier.js",
        "lib/mutationrecorder/CSSStyleVerifier.js",
        "lib/OnMouseOverObserver.js",
        "lib/Utils.js"
    ];
    casper.options.onPageInitialized = function (page) {
        page.injectJs("lib/MouseOverEventListenerObserver.js");
    };
    casper.test.begin("Capture activation link and tooltip element via addEventListener", 2, function (test) {
        casper.start(fixtures_url + "capture_tooltips_test01.html", function () {
            var tooltip_selector = "",
                activator_selector = this.evaluate(function () {
                    window.recorder = OverMutationRecorder(
                        [ClassNameVerifier, CSSStyleVerifier, InnerHTMLVerifier]);
                    return Utils.getSelector(MouseOverEventListenerObserver.popActivator());
                });
            test.assertEquals(activator_selector, "#link2");
            this.mouseEvent("mouseover", activator_selector);
            tooltip_selector = this.evaluate(function () {
                recorder.trackChanges();
                return recorder.popLastEvent().target.id;
            });
            test.assertEquals(tooltip_selector, "useful2");
        });
        casper.run(function () {
            test.done();
        });
    });
    casper.test.begin("Capture activation link and tooltip element via OnMouseOver", 2, function (test) {
        casper.start(fixtures_url + "capture_tooltips_test02.html", function () {
            var tooltip_selector = "",
                activator_selector = this.evaluate(function () {
                    window.recorder = OverMutationRecorder(
                        [ClassNameVerifier, CSSStyleVerifier, InnerHTMLVerifier]);
                    return Utils.getSelector(OnMouseOverObserver.popActivator());
                });
            test.assertEquals(activator_selector, "#link2");
            this.mouseEvent("mouseover", activator_selector);
            tooltip_selector = this.evaluate(function () {
                recorder.trackChanges();
                return recorder.popLastEvent().target.id;
            });
            test.assertEquals(tooltip_selector, "useful2");
        });
        casper.run(function () {
            test.done();
        });
    });
}());
