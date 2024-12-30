window.addEventListener("DOMContentLoaded", () => {
    const alertBox = document.querySelectorAll(".alert");
    alertBox.forEach(alert => {
        if (alert && alert.classList.contains("flex")) {
            setTimeout(() => {
                alert.classList.remove("opacity-0");
            }, 300);
            setTimeout(() => {
                alert.classList.add("opacity-0"); // Hide after 1 second
            }, 2500);
            setTimeout(() => {
                alert.classList.remove("flex");
                alert.classList.add("hidden"); // Hide after 1 second
            }, 5000);
        }
    })
});
document.addEventListener("DOMContentLoaded", () => {
    const deleteButtons = document.querySelectorAll(".delete-button");
    const modal = document.getElementById("deleteModal");
    const confirmButton = document.getElementById("confirmDeleteButton");

    // Add event listener to each delete button
    deleteButtons.forEach(button => {
        button.addEventListener("click", function () {
            const productCode = this.getAttribute("data-product-code");
            confirmButton.href = `/remove?productCode=${productCode}`;
            modal.showModal();
        });
    });
});