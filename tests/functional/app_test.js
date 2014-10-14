(function () {
    var app = require("../../App"),
        fixtures_url = casper.cli.get("fixtures");
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
    casper.test.begin("App should retrieve activation elements and identify mutations", 13, function (test) {
        var casperStartCalled = "", casperCreateCalled = false, captureScreenParams = [],
            fakeCasper = {
                create: function (params) {
                    test.assertEquals(params.clientScripts[0], "lib/mutationrecorder/OverMutationRecorder.js");
                    test.assertEquals(params.clientScripts[1], "lib/mutationrecorder/ClassNameVerifier.js");
                    test.assertEquals(params.clientScripts[2], "lib/mutationrecorder/InnerHTMLVerifier.js");
                    test.assertEquals(params.clientScripts[3], "lib/mutationrecorder/CSSStyleVerifier.js");
                    test.assertEquals(params.clientScripts[4], "lib/OnMouseOverObserver.js");
                    test.assertEquals(params.clientScripts[5], "lib/Utils.js");
                    casperCreateCalled = true;
                },
                start: function () {
                    var self = this, start_arguments = arguments;
                    casperStartCalled = start_arguments[0];
                    casper.start(fixtures_url + "capture_tooltips_test01.html", function () {
                        this.captureScreen = function () {
                            self.captureScreen.apply(self, arguments);
                        };
                        start_arguments[1].apply(this, arguments);
                    });
                },
                captureScreen: function () {
                    captureScreenParams.push(arguments);
                },
                run: function () {
                    casper.run(function () {
                        test.assertEquals(captureScreenParams.length, 2);
                        test.assertEquals(captureScreenParams[0][0], "captured_widgets/widget_activator01.png");
                        test.assertEquals(captureScreenParams[0][1], "#link2");
                        test.assertEquals(captureScreenParams[1][0], "captured_widgets/widget01.png");
                        test.assertEquals(captureScreenParams[1][1], "#useful2");

                        test.done();
                    });
                }
            };
        app.init({ casper: fakeCasper });
        app.captureWidgets("http://abobrinha.com", "captured_widgets/");
        test.assertEquals(casperStartCalled, "http://abobrinha.com");
        test.assert(casperCreateCalled, "casper.create should be called");
    });
    casper.test.begin("App should capture screen for multiple widgets", 13, function (test) {
        var captureScreenParams = [],
            fakeCasper = {
                create: function (params) {},
                start: function () {
                    var self = this, start_arguments = arguments;
                    casper.start(fixtures_url + "capture_tooltips_test03.html", function () {
                        this.captureScreen = function () {
                            self.captureScreen.apply(self, arguments);
                        };
                        start_arguments[1].apply(this, arguments);
                    });
                },
                captureScreen: function () {
                    captureScreenParams.push(arguments);
                },
                run: function () {
                    casper.run(function () {
                        test.assertEquals(captureScreenParams.length, 6);
                        test.assertEquals(captureScreenParams[0][0], "captured_widget/widget_activator01.png");
                        test.assertEquals(captureScreenParams[0][1], "#link2");
                        test.assertEquals(captureScreenParams[1][0], "captured_widget/widget01.png");
                        test.assertEquals(captureScreenParams[1][1], "#useful2");
                        test.assertEquals(captureScreenParams[2][0], "captured_widget/widget_activator02.png");
                        test.assertEquals(captureScreenParams[2][1], "#link4");
                        test.assertEquals(captureScreenParams[3][0], "captured_widget/widget02.png");
                        test.assertEquals(captureScreenParams[3][1], "#useful4");
                        test.assertEquals(captureScreenParams[4][0], "captured_widget/widget_activator03.png");
                        test.assertEquals(captureScreenParams[4][1], "#link3");
                        test.assertEquals(captureScreenParams[5][0], "captured_widget/widget03.png");
                        test.assertEquals(captureScreenParams[5][1], "#useful3");
                        test.done();
                    });
                }
            };
        app.init({ casper: fakeCasper });
        app.captureWidgets("http://abobrinha.com", "captured_widget/");
    });
}());
