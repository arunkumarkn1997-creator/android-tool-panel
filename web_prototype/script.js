// 6 Warp Colors as requested
const warpColors = [
    { name: "Red", value: "#FF0000" },
    { name: "Green", value: "#2CCA56" },
    { name: "Blue", value: "#0096FF" },
    { name: "Yellow", value: "#FFF000" },
    { name: "Orange", value: "#FFFF7F00" }, /* Fixed hex */
    { name: "Purple", value: "#800080" }
];

const appColors = [
    { name: "Green", value: "#2CCA56" },
    { name: "Light green", value: "#D1E231" },
    { name: "Green", value: "#228B22" },
    { name: "Green", value: "#32CD32" },
    { name: "Yellow", value: "#FFF000" },
    { name: "Blue", value: "#0096FF" },
    { name: "Blue", value: "#0047AB" }
];

const swatchList = document.getElementById('swatchList');
const headerPreview = document.getElementById('headerPreview');
const headerSelectedName = document.getElementById('headerSelectedName');
const totalCountBox = document.getElementById('totalCountBox');
const dropdownMenu = document.getElementById('dropdownMenu');
const dropdownSearch = document.getElementById('dropdownSearch');
const dropdownItemsContainer = document.getElementById('dropdownItemsContainer');
const langToggleBtn = document.getElementById('langToggleBtn');
// Static Text Elements to Update
const warpTitle = document.getElementById('warpTitle');
const secondaryLabel = document.querySelector('.Secondary_Label');
const footerLabel = document.querySelector('.Footer_Label');

// State
let isTamil = false;
let selectedWarpColor = warpColors[1]; // Default Green
let selectedWeftCounts = {}; // { index: count }

// Translations
const translations = {
    "Warp colour": "பாவு நிறம்",
    "Suitable Weft colours": "பொருத்தமான ஊடு நிறங்கள்",
    "Total selected :": "Total Selection",
    "Red": "சிகப்பு",
    "Green": "பச்சை",
    "Blue": "நீலம்",
    "Yellow": "மஞ்சள்",
    "Orange": "ஆரஞ்சு",
    "Purple": "ஊதா",
    "Light green": "இளம் பச்சை",
    "No colors found": "நிறங்கள் இல்லை",
    "Search color...": "நிறத்தைத் தேடுங்கள்..."
};

// Concise Tamil Footer
const t_Tamil_Footer = "மொத்தம் :";

function t(text) {
    if (isTamil && text === "Total selected :") return t_Tamil_Footer;
    return isTamil ? (translations[text] || text) : text;
}

function toggleLanguage() {
    isTamil = !isTamil;
    langToggleBtn.classList.toggle('active', isTamil);
    render(); // Re-render everything with new language
}

// Search Listener
dropdownSearch.addEventListener('input', (e) => {
    renderDropdown(e.target.value);
});

function updateLanguageToggle() {
    const btn = document.getElementById('langToggleBtn');
    if (btn) {
        // If Tamil (isTamil=true), show 'EN' to switch back
        // If English (isTamil=false), show 'த' to switch to Tamil
        btn.innerText = isTamil ? "EN" : "த";

        if (isTamil) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    }
}

function toggleDropdown() {
    const isShowing = dropdownMenu.classList.contains('show');
    if (isShowing) {
        dropdownMenu.classList.remove('show');
    } else {
        dropdownMenu.classList.add('show');
        dropdownSearch.value = ''; // Reset search
        dropdownSearch.placeholder = t("Search color...");
        dropdownSearch.focus();
        renderDropdown();
    }
}

function selectWarpColor(index) {
    selectedWarpColor = warpColors[index];
    dropdownMenu.classList.remove('show');
    renderHeader();
}

function renderHeader() {
    headerPreview.style.backgroundColor = selectedWarpColor.value;
    headerSelectedName.textContent = t(selectedWarpColor.name);

    // Update Static Header Texts
    warpTitle.textContent = t("Warp colour");
    secondaryLabel.textContent = t("Suitable Weft colours");
}

function renderDropdown(filter = "") {
    dropdownItemsContainer.innerHTML = '';

    const filteredColors = warpColors.filter(c =>
        c.name.toLowerCase().includes(filter.toLowerCase()) ||
        t(c.name).toLowerCase().includes(filter.toLowerCase())
    );

    if (filteredColors.length === 0) {
        const noResult = document.createElement('div');
        noResult.style.padding = "12px";
        noResult.style.color = "#999";
        noResult.textContent = t("No colors found");
        dropdownItemsContainer.appendChild(noResult);
        return;
    }

    filteredColors.forEach((color) => {
        const originalIndex = warpColors.indexOf(color);
        const item = document.createElement('div');
        item.className = 'Dropdown_Item';
        item.onclick = () => selectWarpColor(originalIndex);
        item.innerHTML = `
            <div class="Row_Swatch" style="background-color: ${color.value}; width: 28px; height: 28px;"></div>
            <span>${t(color.name)}</span>
        `;
        dropdownItemsContainer.appendChild(item);
    });
}


function render() {
    renderHeader();
    updateLanguageToggle(); // Update button text
    footerLabel.textContent = isTamil ? "மொத்தம் :" : "Total Selected :";

    swatchList.innerHTML = '';
    appColors.forEach((color, index) => {
        const row = document.createElement('div');
        row.className = 'Weft_Color_Row';
        const count = selectedWeftCounts[index] || 0;

        // Swapped + and - : Plus on Left (First), Minus on Right (Last)
        row.innerHTML = `
            <div class="Row_Left">
                <div class="Row_Swatch" style="background-color: ${color.value}"></div>
                <div class="Row_Label">${t(color.name)}</div>
            </div>
            <div class="Row_Right">
                <div class="Counter_Btn" onclick="updateCount(${index}, 1)">+</div>
                <input type="text" class="Counter_Box" value="${count}" 
                       onblur="handleCountInput(${index}, this.value)" 
                       onkeypress="handleCountKeypress(event, ${index}, this.value)"
                       onclick="this.select()">
                <div class="Counter_Btn" onclick="updateCount(${index}, -1)">–</div>
            </div>
        `;
        swatchList.appendChild(row);
    });

    const total = Object.values(selectedWeftCounts).reduce((a, b) => a + b, 0);
    totalCountBox.textContent = total;
}

function updateCount(index, delta) {
    const current = selectedWeftCounts[index] || 0;
    const newCount = Math.max(0, current + delta); // Prevent negative

    if (newCount === 0) {
        delete selectedWeftCounts[index];
    } else {
        selectedWeftCounts[index] = newCount;
    }
    render();
}

function handleCountInput(index, value) {
    const numValue = parseInt(value, 10);

    if (isNaN(numValue) || numValue < 0) {
        // Invalid input, reset to current value
        render();
        return;
    }

    if (numValue === 0) {
        delete selectedWeftCounts[index];
    } else {
        selectedWeftCounts[index] = numValue;
    }
    render();
}

function handleCountKeypress(event, index, value) {
    if (event.key === 'Enter') {
        event.preventDefault();
        event.target.blur(); // Trigger onblur handler
    }
}

// Global scope
window.updateCount = updateCount;
window.toggleDropdown = toggleDropdown;
window.toggleLanguage = toggleLanguage;
window.handleCountInput = handleCountInput;
window.handleCountKeypress = handleCountKeypress;

// Initial Render
render();
