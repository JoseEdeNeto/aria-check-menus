(function () {
    window.Utils = (function () {
        var getNodeIndex;
        getElementIndex = function (element) {
            var parentNode = element.parentNode;
            for (var i = 0; i < parentNode.children.length; i++) {
                if (parentNode.children.item(i) === element)
                    return i + 1;
            };
            return null;
        }

        return {
            getSelector: function (target) {
                var selector = target.tagName.toLowerCase(),
                    parentNode = null;
                selector += ":nth-child(" + getElementIndex(target) + ")";
                while (target.parentNode.tagName != "HTML") {
                    var parentSelector = target.parentNode.tagName.toLowerCase() +
                                         ":nth-child(" + getElementIndex(target.parentNode) + ")";
                    selector = parentSelector + " > " + selector;
                    target = target.parentNode;
                }
                return selector;
            }
        };
    }());
}());
