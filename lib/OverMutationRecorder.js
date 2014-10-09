(function () {
    window.OverMutationRecorder = (function () {
        var recorded_mutations = [],
            observer, observer_params = { attributes: true },
            elements = document.getElementsByTagName("*");
        observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                recorded_mutations.push(mutation);
            });
        });

        for (var i = 0; i < elements.length; i++) {
            if (elements[i].nodeType == 1)
                observer.observe(elements[i], observer_params);
        };

        return {
            popLastEvent: function () {
                return 0;
            }
        };
    }());
}());
