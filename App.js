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
            tooltip_selector = "", captured_selectors = [],
            activator_selector, widgets_activator_number;

        widgets_activator_number = self.evaluate(function () {
            return MouseOverEventListenerObserver.size() + OnMouseOverObserver.size();
        });

        for (var i = 0; i < widgets_activator_number; i++) {
            (function () {
                var index = i;
                self.wait(wait * (index + 1) + 5, function () {
                    activator_selector = self.evaluate(function () {
                        var target = OnMouseOverObserver.popActivator();
                        window.recorder = OverMutationRecorder(
                            [ClassNameVerifier, CSSStyleVerifier, InnerHTMLVerifier]);
                        if (target)
                            return Utils.getSelector(target);
                        return Utils.getSelector(MouseOverEventListenerObserver.popActivator());
                    });
                    if (captured_selectors.indexOf(activator_selector) === -1) {
                        self.captureSelector(folder + "activator0" + (index + 1) + ".png", activator_selector);
                        captured_selectors.push(activator_selector);
                        self.mouseEvent("mouseover", activator_selector);
                        tooltip_selector = self.evaluate(function () {
                            var mutation, selectors = [];
                            recorder.trackChanges();
                            while ((mutation = recorder.popLastEvent()) != null)
                                selectors.push(Utils.getSelector(mutation.target));
                            return selectors;
                        });
                        self.wait(wait, function () {
                            for (var i = 0; i < tooltip_selector.length; i++) {
                                self.captureSelector(folder + "widget0" + (index + 1) +
                                                     "-" + (i + 1) + ".png", tooltip_selector[i]);
                            };
                        });
                    }
                });
            }());
        };
    });
    casperInstance.run();
};
