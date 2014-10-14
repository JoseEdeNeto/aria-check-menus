var casperInstance = null, observers = [], recorder = null;

exports.init = function (params) {
    casperInstance = params.casper;
    casperInstance = casperInstance.create({
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
    return casperInstance;
};
exports.captureWidgets = function (url, folder) {
    casperInstance.start(url, function () {
        var tooltip_selector = "",
            activator_selector, widgets_activator_number;

        widgets_activator_number = this.evaluate(function () {
            return MouseOverEventListenerObserver.size() + OnMouseOverObserver.size();
        });

        for (var i = 1; i <= widgets_activator_number; i++) {
            activator_selector = this.evaluate(function () {
                var target = OnMouseOverObserver.popActivator();
                window.recorder = OverMutationRecorder(
                    [ClassNameVerifier, CSSStyleVerifier, InnerHTMLVerifier]);
                if (target)
                    return Utils.getSelector(target);
                return Utils.getSelector(MouseOverEventListenerObserver.popActivator());
            });
            this.captureSelector(folder + "widget_activator0" + i + ".png", activator_selector);
            this.mouseEvent("mouseover", activator_selector);
            tooltip_selector = this.evaluate(function () {
                recorder.trackChanges();
                return recorder.popLastEvent().target.id;
            });
            this.captureSelector(folder + "widget0" + i + ".png", "#" + tooltip_selector);
        };
    });
    casperInstance.run();
};
