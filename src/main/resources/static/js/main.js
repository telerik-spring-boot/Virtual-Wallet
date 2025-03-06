const tooltipTriggerList = document.querySelectorAll(
  '[data-bs-toggle="tooltip"]'
);
const tooltipList = [...tooltipTriggerList].map(
  (tooltipTriggerEl) => new bootstrap.Tooltip(tooltipTriggerEl)
);

const popoverTriggerList = document.querySelectorAll(
  '[data-bs-toggle="popover"]'
);
const popoverList = [...popoverTriggerList].map(
  (popoverTriggerEl) => new bootstrap.Popover(popoverTriggerEl)
);

const pure = new PureCounter();
new PureCounter();

// general js project js
$(function () {
  "use strict";
  const root = document.documentElement;

  // main sidebar menu toggle js
  $(".sidebar-toggle-btn").on("click", function () {
    $("body").toggleClass("sidebar-hide");
  });

  $(".sidebar-toggle").on("click", function () {
    $(".sidebar").toggleClass("open");
  });

  $(".menu-toggle").on("click", function () {
    $("body").toggleClass("sidebar-mini");
    if ($("body").hasClass("sidebar-mini")) {
      $("#Plugin").removeClass("show");
    } else {
      $("#Plugin").addClass("show");
    }
  });

  // sidebar menu mini
  $(".mini-sidebar input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $("body").addClass("sidebar-mini");
    } else {
      $("body").removeClass("sidebar-mini");
    }
  });

  // main theme color setting js
  $(".themeSetting li").on("click", function () {
    const $body = $("body");
    const $this = $(this);
    const existTheme = $(".themeSetting li.active").data("theme");
    $(".themeSetting li").removeClass("active");
    $this.addClass("active");
    $body.attr("data-theme", "theme-" + $this.data("theme"));
  });

  // card full screen js
  const DIV_CARD = "div.card";
  $(".card-fullscreen").on("click", function (e) {
    const $card = $(this).closest(DIV_CARD);
    $card.toggleClass("fullscreen");
    e.preventDefault();
    return false;
  });

  /** Function for remove card */
  $('[data-toggle="card-remove"]').on("click", function (e) {
    var $card = $(this).closest(DIV_CARD);
    $card.remove();
    e.preventDefault();
    return false;
  });

  // code-show
  $(".card-toggle-one input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".card-main-one").addClass("show");
    } else {
      $(".card-main-one").removeClass("show");
    }
  });

  $(".card-toggle-two input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".card-main-two").addClass("show");
    } else {
      $(".card-main-two").removeClass("show");
    }
  });

  $(".card-toggle-three input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".card-main-three").addClass("show");
    } else {
      $(".card-main-three").removeClass("show");
    }
  });

  $(".card-toggle-four input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".card-main-four").addClass("show");
    } else {
      $(".card-main-four").removeClass("show");
    }
  });

  // table-show
  $(".table-toggle-one input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".table-main-one").addClass("show");
    } else {
      $(".table-main-one").removeClass("show");
    }
  });

  $(".table-toggle-two input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".table-main-two").addClass("show");
    } else {
      $(".table-main-two").removeClass("show");
    }
  });

  $(".table-toggle-three input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".table-main-three").addClass("show");
    } else {
      $(".table-main-three").removeClass("show");
    }
  });

  $(".table-toggle-four input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".table-main-four").addClass("show");
    } else {
      $(".table-main-four").removeClass("show");
    }
  });

  $(".table-toggle-five input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".table-main-five").addClass("show");
    } else {
      $(".table-main-five").removeClass("show");
    }
  });

  $(".table-toggle-six input:checkbox").on("click", function () {
    if ($(this).is(":checked")) {
      $(".table-main-six").addClass("show");
    } else {
      $(".table-main-six").removeClass("show");
    }
  });

  // Function to toggle theme and store in localStorage
  function toggleTheme(selector) {
    const element = $(selector);
    const currentTheme = element.attr("data-bs-theme");
    const newTheme = currentTheme === "none" ? "dark" : "none";

    element.attr("data-bs-theme", newTheme);
    localStorage.setItem(`${selector}-theme`, newTheme);
  }

  // Apply stored theme on page load
  function applyStoredTheme(selector) {
    const storedTheme = localStorage.getItem(`${selector}-theme`);
    if (storedTheme) {
      $(selector).attr("data-bs-theme", storedTheme);
    }
  }

  // Function to toggle classes and store in localStorage
  function toggleClass(selector, className, stateKey) {
    const element = $(selector);
    const isActive = element.hasClass(className);

    element.toggleClass(className);
    localStorage.setItem(stateKey, !isActive);
  }

  // Apply stored classes on page load
  function applyStoredClass(selector, className, stateKey) {
    const storedState = localStorage.getItem(stateKey) === "true";
    if (storedState) {
      $(selector).addClass(className);
    }
  }

  // Event listeners for toggles
  $(".sidebar-toggle input:checkbox").on("click", function () {
    toggleTheme(".sidebar");
  });

  $(".header-toggle input:checkbox").on("click", function () {
    toggleTheme("header");
  });

  // SVG Icon Stroke
  $(".svg-stroke input:radio").on("click", function () {
    const selectedClass = this.value;
    const others = $("[name='" + this.name + "']")
      .map(function () {
        return this.value;
      })
      .get()
      .join(" ");

    $("body").removeClass(others).addClass(selectedClass);
    localStorage.setItem("svg-stroke", selectedClass);
  });

  // Gradient Background
  $(".bodygradient-toggle input:checkbox").on("click", function () {
    toggleClass("body", "bg-gradient", "body-gradient");
  });

  // Layout Border
  $(".border-toggle input:checkbox").on("click", function () {
    toggleClass("body", "layout-border", "layout-border");
  });

  // Monochrome Mode
  $(".monochrome-toggle input:checkbox").on("click", function () {
    toggleClass("body", "monochrome", "monochrome-mode");
  });

  // Radius-0
  $(".radius-toggle input:checkbox").on("click", function () {
    toggleClass("body", "radius-0", "radius-mode");
  });

  // Sidebar Icon Color
  $(".svg-icon-color input:checkbox").on("click", function () {
    toggleClass(".menu-list", "icon-color", "sidebar-icon-color");
  });

  // Card Shadow
  $(".cb-shadow input:checkbox").on("click", function () {
    toggleClass(".card", "shadow-active", "card-shadow");
  });

  // Apply stored preferences on page load
  $(document).ready(function () {
    applyStoredTheme("header");

    const svgStrokeClass = localStorage.getItem("svg-stroke");
    if (svgStrokeClass) {
      $("body").addClass(svgStrokeClass);
    }

    applyStoredClass("body", "bg-gradient", "body-gradient");
    applyStoredClass("body", "layout-border", "layout-border");
    applyStoredClass("body", "monochrome", "monochrome-mode");
    applyStoredClass("body", "radius-0", "radius-mode");
    applyStoredClass(".menu-list", "icon-color", "sidebar-icon-color");
    applyStoredClass(".card", "shadow-active", "card-shadow");
  });

  // Password Meter
  $(".password-meter .form-control").on("input", function () {
    var matchedCriteria = 0;
    var password = $(this).val();
    var upperCase = new RegExp("[A-Z]");
    var lowerCase = new RegExp("[a-z]");
    var numbers = new RegExp("[0-9]");
    var specialChars = new RegExp("^(?=.*?[#?!@$%^&*-]).{1,}$");
    if (password.length > 7) {
      matchedCriteria++;
    }
    if (password.length > 0 && password.match(upperCase)) {
      matchedCriteria++;
    }
    if (password.length > 0 && password.match(lowerCase)) {
      matchedCriteria++;
    }
    if (password.length > 0 && password.match(numbers)) {
      matchedCriteria++;
    }
    if (password.length > 0 && password.match(specialChars)) {
      matchedCriteria++;
    }
    $(".password-meter .progress-bar")[0].style.width =
      matchedCriteria * 20 + "%";
  });
  // Image file input
  $(".image-input .form-control").on("change", function () {
    var url = URL.createObjectURL(this.files[0]);
    $(this).parent().parent().children(".avatar-wrapper")[0].style.background =
      "url(" + url + ") no-repeat";
  });
  // table select all checkbox js
  $(".select-all.form-check-input").on("change", function () {
    //row-selectable
    var isChecked = $(this).is(":checked");
    var rows = $(this)
      .parent()
      .parent()
      .parent()
      .parent()
      .parent()
      .find(".row-selectable");
    if (rows.length > 0) {
      rows.each(function (index) {
        $(this).find(".form-check-input")[0].checked = isChecked;
      });
    }
  });

  $(".deleterow").on("click", function () {
    var tablename = $(this).closest("table").DataTable();
    tablename.row($(this).parents("tr")).remove().draw();
  });
});

// Light/Dark
/*!
 * Color mode toggler for Bootstrap's docs (https://getbootstrap.com/)
 * Copyright 2011-2022 The Bootstrap Authors
 * Licensed under the Creative Commons Attribution 3.0 Unported License.
 */

(() => {
  "use strict";
  const storedTheme = localStorage.getItem("theme");
  const getPreferredTheme = () => {
    if (storedTheme) {
      return storedTheme;
    }

    return window.matchMedia("(prefers-color-scheme: dark)").matches
      ? "dark"
      : "light";
  };
  const setTheme = function (theme) {
    if (
      theme === "auto" &&
      window.matchMedia("(prefers-color-scheme: dark)").matches
    ) {
      document.documentElement.setAttribute("data-bs-theme", "dark");
    } else {
      document.documentElement.setAttribute("data-bs-theme", theme);
    }
  };
  setTheme(getPreferredTheme());
  const showActiveTheme = (theme) => {
    const activeThemeIcon = document.querySelector(".theme-icon-active use");
    const btnToActive = document.querySelector(
      `[data-bs-theme-value="${theme}"]`
    );
    const svgOfActiveBtn = btnToActive
      .querySelector("svg use")
      .getAttribute("href");

    document.querySelectorAll("[data-bs-theme-value]").forEach((element) => {
      element.classList.remove("active");
    });
    if (svgOfActiveBtn) {
      btnToActive.classList.add("active");
      activeThemeIcon.setAttribute("href", svgOfActiveBtn);
    } else {
      console.error("Element not found");
    }
    btnToActive.classList.add("active");
    activeThemeIcon.setAttribute("href", svgOfActiveBtn);
  };
  window
    .matchMedia("(prefers-color-scheme: dark)")
    .addEventListener("change", () => {
      if (storedTheme !== "light" || storedTheme !== "dark") {
        setTheme(getPreferredTheme());
      }
    });
  window.addEventListener("DOMContentLoaded", () => {
    showActiveTheme(getPreferredTheme());

    document.querySelectorAll("[data-bs-theme-value]").forEach((toggle) => {
      toggle.addEventListener("click", () => {
        const theme = toggle.getAttribute("data-bs-theme-value");
        localStorage.setItem("theme", theme);
        setTheme(theme);
        showActiveTheme(theme);
      });
    });
  });
})();

// focus Authentication all manu list anchor tag active class remove
$(document).ready(() => {
  $(".menu-list  .dropdown-toggle").click(() => {
    $(".menu-list a").removeClass("active");
  });
});
