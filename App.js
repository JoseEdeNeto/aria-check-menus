var casperInstance = null, observers = [], recorder = null, wait = 1;

exports.init = function (params) {
    casperInstance = params.casper;
    wait = params.wait ? params.wait : 1;
    casperInstance = casperInstance.create({
        clientScripts: [
            "lib/mutationrecorder/OverMutationRecorder.js",
            "lib/mutationrecorder/ClassNameVerifier.js",
            "lib/mutationrecorder/InnerHTMLVerifier.js",
            "lib/mutationrecorder/CSSStyleVerifier.js",
            "lib/OnMouseOverObserver.js",
            "lib/Utils.js"
        ],
        viewportSize: {
            width: 1024, height: 768
        },
        onPageInitialized: function (page) {
            page.injectJs("lib/MouseOverEventListenerObserver.js");
        }
    });
    return casperInstance;
};
exports.captureWidgets = function (url, folder) {
    casperInstance.start(url, function () {
        var self = this,
            tooltip_selector = "",
            activator_selector, widgets_activator_number;

        widgets_activator_number = self.evaluate(function () {
            return MouseOverEventListenerObserver.size() + OnMouseOverObserver.size();
        });

        for (var i = 1; i <= widgets_activator_number; i++) {
            (function () {
                var index = i;
                self.wait(wait * index + 5, function () {
                    activator_selector = self.evaluate(function () {
                        var target = OnMouseOverObserver.popActivator();
                        window.recorder = OverMutationRecorder(
                            [ClassNameVerifier, CSSStyleVerifier, InnerHTMLVerifier]);
                        if (target)
                            return Utils.getSelector(target);
                        return Utils.getSelector(MouseOverEventListenerObserver.popActivator());
                    });
                    self.captureSelector(folder + "widget_activator0" + index + ".png", activator_selector);
                    self.mouseEvent("mouseover", activator_selector);
                    tooltip_selector = self.evaluate(function () {
                        recorder.trackChanges();
                        return recorder.popLastEvent().target.id;
                    });
                    self.wait(wait, function () {
                        self.captureSelector(folder + "widget0" + index + ".png", "#" + tooltip_selector);
                    });
                });
            }());
        };
    });
    casperInstance.run();
};
