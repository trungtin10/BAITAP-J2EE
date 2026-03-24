// Custom JavaScript - Course Registration

document.addEventListener('DOMContentLoaded', function() {
    // Auto-dismiss alerts after 5 seconds (requires Bootstrap 5)
    if (typeof bootstrap !== 'undefined') {
        document.querySelectorAll('.alert-dismissible').forEach(function(el) {
            setTimeout(function() {
                try {
                    const bsAlert = bootstrap.Alert.getOrCreateInstance(el);
                    if (bsAlert) bsAlert.close();
                } catch (e) {}
            }, 5000);
        });
    }
});
