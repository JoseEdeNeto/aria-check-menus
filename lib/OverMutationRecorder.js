(function () {
    window.OverMutationRecorder = (function () {
        var recorded_mutations = [],
            elements_representations = [],
            all_elements = document.getElementsByTagName("*"),
            private_methods = {};

        private_methods.save_state = function () {
            for (var i = 0; i < all_elements.length; i++) {
                elements_representations[i] = { style: {} };
                for (var j in all_elements[i].style) {
                    if (all_elements[i].style[j] !== null)
                        elements_representations[i].style[j] = all_elements[i].style[j].toString();
                    else
                        elements_representations[i].style[j] = "";
                };
                elements_representations[i].className = all_elements[i].className;
            };
        };

        private_methods.track_changes = function () {
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
                if (all_elements[i].className !== elements_representations[i].className) {
                    recorded_mutations.push({
                        target: all_elements[i],
                        type: "style"
                    });
                }
            };
        };

        private_methods.save_state();

        return {
            popLastEvent: function () {
                private_methods.track_changes();
                return recorded_mutations.pop();
            }
        };
    }());
}());
