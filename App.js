var casperInstance = null, observers = [], recorder = null;

exports.init = function (params) {
    casperInstance = params.casper;
    casperInstance.create({
        clientScripts: [
            "lib/mutationrecorder/OverMutationRecorder.js",
            "lib/mutationrecorder/ClassNameVerifier.js",
            "lib/mutationrecorder/InnerHTMLVerifier.js",
            "lib/mutationrecorder/CSSStyleVerifier.js",
            "lib/OnMouseOverObserver.js",
            "lib/Utils.js"
        ],
        onPageInitialized: function (page) {
            page.injectJs("lib/MouseOverEventListenerObserver.js");
        }
    });
};
exports.captureWidgets = function (url) {
    casperInstance.start(url, function () {
        var tooltip_selector = "",
            activator_selector = this.evaluate(function () {
                window.recorder = OverMutationRecorder(
                    [ClassNameVerifier, CSSStyleVerifier, InnerHTMLVerifier]);
                return Utils.getSelector(MouseOverEventListenerObserver.popActivator());
            });
        this.captureScreen("widget_activator01.png", activator_selector);
        this.mouseEvent("mouseover", activator_selector);
        tooltip_selector = this.evaluate(function () {
            recorder.trackChanges();
            return recorder.popLastEvent().target.id;
        });
        this.captureScreen("widget01.png", "#" + tooltip_selector);
    });
    casperInstance.run();
};
