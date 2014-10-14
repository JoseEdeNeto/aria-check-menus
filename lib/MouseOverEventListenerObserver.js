(function () {
    window.MouseOverEventListenerObserver = (function () {
        var observed = [],
            trueAddEventListener = HTMLElement.prototype.addEventListener;

        HTMLElement.prototype.addEventListener = function (type) {
            if (type === "mouseover")
                observed.push(this);
            return trueAddEventListener.apply(this, arguments);
        };

        return {
            size: function () { return observed.length; },
            popActivator: function () { return observed.pop(); }
        };
    }());
}());
