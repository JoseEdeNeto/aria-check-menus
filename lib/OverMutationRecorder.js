(function () {
    window.OverMutationRecorder = (function () {
        var recorded_mutations = [],
            elements_representations = [],
            all_elements = document.getElementsByTagName("*"),
            track_changes = function () {};

        for (var i = 0; i < all_elements.length; i++) {
            elements_representations[i] = { style: {} };
            for (var j in all_elements[i].style) {
                if (all_elements[i].style[j] !== null)
                    elements_representations[i].style[j] = all_elements[i].style[j].toString();
                else
                    elements_representations[i].style[j] = "";
            };
        };

        track_changes = function () {
            for (var i = 0; i < all_elements.length; i++) {
                for (var j in all_elements[i].style) {
                    if ((all_elements[i].style[j] === null && elements_representations[i].style[j] !== "") ||
                        (all_elements[i].style[j] !== null && elements_representations[i].style[j] !== all_elements[i].style[j].toString())) {
                        recorded_mutations.push({
                            target: all_elements[i],
                            type: "style"
                        });
                        break;
                    }
                };
            };
        };

        return {
            popLastEvent: function () {
                track_changes();
                return recorded_mutations.pop();
            }
        };
    }());
}());
