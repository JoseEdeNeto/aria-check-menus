var webpage = require("webpage"),
    system = require("system"),
    page = webpage.create();

if (system.args.length < 2) {
    console.log("Arguments missing...");
    phantom.exit();
}
page.onError = function () {};
page.onInitialized = function () {
    page.evaluate(function () {
        var true_addEventListener = HTMLElement.prototype.addEventListener,
            hover_events = ["mouseover"];
        window.events = [];
        HTMLElement.prototype.addEventListener = function (ev_type) {
            if (hover_events.indexOf(ev_type) >= 0) {
                var target = this,
                    selector = "";

                while (target.parentElement != null) {
                    selector = (selector.length === 0 ?
                        target.tagName.toLowerCase() :
                        target.tagName.toLowerCase() + " > " + selector);
                    target = target.parentElement;
                }
                if (selector.length > 0)
                    window.events.push(selector);
            }
            true_addEventListener.apply(this, arguments);
        };
    });
};
page.settings.XSSAuditingEnabled = true;
page.settings.webSecurityEnabled = false;
page.open(system.args[1], function () {
    setTimeout(function () {
        console.log(page.evaluate(function () {
            // onmouseover listeners
            var all = document.querySelectorAll("*");
            for (var l = 0; l < all.length; l++) {
                if (all[l].onmouseover) {
                    var target = all[l],
                        selector = "";

                    while (target.parentElement != null) {
                        selector = (selector.length === 0 ?
                            target.tagName.toLowerCase() :
                            target.tagName.toLowerCase() + " > " + selector);
                        target = target.parentElement;
                    }
                    if (selector.length > 0)
                        window.events.push(selector);
                }
            };

            // CSS :hover selectors
            for (var i = 0; i < document.styleSheets.length; i++) {
                for (var j = 0; document.styleSheets[i].cssRules &&
                                j < document.styleSheets[i].cssRules.length; j++) {
                    if (document.styleSheets[i].cssRules[j].selectorText) {
                        var rules = document.styleSheets[i].cssRules[j].selectorText.split(",");
                        for (var k = 0; k < rules.length; k++) {
                            if (rules[k].search(":hover") > 0) {
                                var s = rules[k].substring(0, rules[k].search(":hover")).trim();
                                try {
                                    document.querySelector(s);
                                } catch (e) { s += " *"; }
                                window.events.push(s);
                            }
                        };
                    }
                }
            }
            return window.events.join(",");
        }));
        phantom.exit();
    }, 10000);
});
