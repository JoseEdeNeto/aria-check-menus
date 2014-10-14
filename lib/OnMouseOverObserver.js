(function () {
    window.OnMouseOverObserver = (function () {
        var observed_elements = [],
            nodes = document.getElementsByTagName("*");

        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].nodeType == 1 && nodes[i].onmouseover) {
                observed_elements.push(nodes[i]);
            }
        };

        return {
            size: function () { return observed_elements.length; },
            popActivator: function () { return observed_elements.pop(); }
        };
    }());
}());
