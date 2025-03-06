/*=============== active class ==============*/
document.addEventListener("DOMContentLoaded", function () {
  const currentLocation = window.location.pathname.split("/").pop();
  const menuItems = document.querySelectorAll(".menu-list a");
  menuItems.forEach((item) => {
    const href = item.getAttribute("href");
    if (
      href === currentLocation ||
      (href === "index.html" && currentLocation === "")
    ) {
      item.classList.add("active");
    }
  });
});
