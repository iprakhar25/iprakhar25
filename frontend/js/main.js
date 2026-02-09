// Configuration
const API_BASE_URL = 'https://iprakhar25.onrender.com/api';

// State Management
const state = {
    isLoggedIn: false,
    currentUser: null,
    token: null,
    isSignUpMode: false,
};

// ========================
// Utility Functions
// ========================

function getYear() {
    return new Date().getFullYear();
}

// Initialize app
document.addEventListener('DOMContentLoaded', async () => {
    // Set footer year
    const yearElement = document.getElementById('year');
    if (yearElement) {
        yearElement.textContent = getYear();
    }
    initializeApp();
    await loadInitialData();  // Wait for data to load before starting counter
    setupEventListeners();
    restoreAuthState();
    startTimeCounter();

    // Polling for real-time updates (Visitor count and Music)
    setInterval(async () => {
        await loadVisitorCount();
    }, 5000); // Every 5 seconds
});

// ========================
// Initialization
// ========================

function initializeApp() {
    console.log('Initializing Portfolio App...');
    // Check if user is already logged in
    const storedToken = localStorage.getItem('authToken');
    if (storedToken) {
        state.token = storedToken;
        state.isLoggedIn = true;
        const user = JSON.parse(localStorage.getItem('currentUser'));
        if (user) {
            state.currentUser = user;
            updateUIForLoggedIn();
        }
    }
}

function setupEventListeners() {
    // Auth buttons
    document.getElementById('authBtn').addEventListener('click', openAuthModal);
    document.getElementById('contactBtn').addEventListener('click', () => {
        if (!state.isLoggedIn) {
            openAuthModal();
        } else {
            document.getElementById('contact').scrollIntoView({ behavior: 'smooth' });
        }
    });
    document.getElementById('contactSignInBtn').addEventListener('click', openAuthModal);

    // Auth form
    document.getElementById('authForm').addEventListener('submit', handleAuthSubmit);
    document.getElementById('toggleAuthMode').addEventListener('click', toggleAuthMode);

    // Contact form
    document.getElementById('messageForm').addEventListener('submit', handleContactSubmit);

    // Modal close
    document.getElementById('authModal').addEventListener('click', (e) => {
        if (e.target.id === 'authModal') {
            document.getElementById('authModal').classList.add('hidden');
        }
    });
}

// ========================
// Data Loading
// ========================

async function loadInitialData() {
    try {
        // Load visitor count
        await loadVisitorCount();

        // Increment visitor count
        await incrementVisitor();

        // Load projects
        await loadProjects();

        // Load skills
        await loadSkills();

        // Load time on earth
        await loadTimeOnEarth();



        // Load LeetCode activity
        await loadLeetCodeActivity();
    } catch (error) {
        console.error('Error loading initial data:', error);
    }
}

async function loadVisitorCount() {
    try {
        const response = await fetch(`${API_BASE_URL}/visitors/count`);
        const data = await response.json();
        const count = data.totalVisitors;

        document.getElementById('visitorCount').textContent = formatNumber(count);
        document.getElementById('footerVisitors').textContent = formatNumber(count);
    } catch (error) {
        console.error('Error loading visitor count:', error);
    }
}

async function incrementVisitor() {
    try {
        await fetch(`${API_BASE_URL}/visitors/increment`, {
            method: 'POST'
        });
        // Reload count after incrementing
        await loadVisitorCount();
    } catch (error) {
        console.error('Error incrementing visitor count:', error);
    }
}

async function loadProjects() {
    try {
        const response = await fetch(`${API_BASE_URL}/projects`);
        const projects = await response.json();

        const grid = document.getElementById('projectsGrid');
        grid.innerHTML = '';

        projects.forEach(project => {
            const card = createProjectCard(project);
            grid.appendChild(card);
        });
    } catch (error) {
        console.error('Error loading projects:', error);
    }
}

async function loadSkills() {
    try {
        const response = await fetch(`${API_BASE_URL}/skills`);
        const skills = await response.json();

        const grid = document.getElementById('skillsGrid');
        if (!grid) return;

        grid.innerHTML = '';

        // Render all skills into the single grid
        skills.forEach(skill => {
            const pill = createSkillPill(skill);
            grid.appendChild(pill);
        });
    } catch (error) {
        console.error('Error loading skills:', error);
    }
}

async function loadTimeOnEarth() {
    try {
        const response = await fetch(`${API_BASE_URL}/time-on-earth`);
        const data = await response.json();
        // Initial value will be set by counter
        window.timeData = data;
    } catch (error) {
        console.error('Error loading time on earth:', error);
    }
}



async function loadLeetCodeActivity() {
    try {
        const response = await fetch(`${API_BASE_URL}/leetcode/stats?username=iprakhar25`);
        if (!response.ok) throw new Error('Failed to fetch LeetCode stats');
        const stats = await response.json();

        // Update counts
        document.getElementById('easyCount').textContent = stats.easySolved;
        document.getElementById('mediumCount').textContent = stats.mediumSolved;
        document.getElementById('hardCount').textContent = stats.hardSolved;

        // Render Grid
        renderContributionGrid(stats.submissionCalendar || {});

    } catch (error) {
        console.error('Error loading LeetCode activity:', error);
    }
}

function renderContributionGrid(calendar) {
    const grid = document.getElementById('contributionGrid');
    const monthLabelsRow = document.getElementById('monthLabels');
    if (!grid || !monthLabelsRow) return;

    grid.innerHTML = '';
    monthLabelsRow.innerHTML = '';

    const today = new Date();
    const startDate = new Date();
    // Exactly 365 days total (including today)
    startDate.setDate(today.getDate() - 364);

    let current = new Date(startDate);
    let totalSubmissions = 0;
    let currentMonth = -1;
    let weekCol = null;

    // 365 days = 52 full weeks + 1 day = 53 columns total
    const totalDays = 365;
    const numCols = Math.ceil(totalDays / 7);

    for (let i = 0; i < totalDays; i++) {
        // Start a new column every 7 days
        if (i % 7 === 0) {
            weekCol = document.createElement('div');
            weekCol.className = 'week-column flex-1 flex flex-col gap-[4px] min-w-[8px]';
            grid.appendChild(weekCol);
        }

        // Check for month transition to add label
        const monthNum = current.getMonth();
        if (monthNum !== currentMonth) {
            const colIndex = Math.floor(i / 7);
            const labelPos = (colIndex / numCols) * 100;

            // Only add label if it's the first or significantly distant from the previous label (~8% of total width)
            if (currentMonth === -1 || (labelPos - window.lastLabelPos > 8)) {
                currentMonth = monthNum;
                window.lastLabelPos = labelPos;

                const label = document.createElement('div');
                label.className = 'absolute whitespace-nowrap text-dark-500 hover:text-dark-300 transition-colors cursor-default';
                label.style.left = `${labelPos}%`;
                label.textContent = current.toLocaleString('default', { month: 'short' });
                monthLabelsRow.appendChild(label);
            }
        }

        const dayCell = document.createElement('div');
        dayCell.className = 'day-cell w-full aspect-square bg-[#161b22] rounded-[2px] border border-white/5 transition-all duration-200 hover:scale-125 hover:z-10 cursor-pointer';

        const dateStr = current.toISOString().split('T')[0];
        let count = 0;
        for (const [ts, submissions] of Object.entries(calendar)) {
            const tsDate = new Date(parseInt(ts) * 1000).toISOString().split('T')[0];
            if (tsDate === dateStr) {
                count = submissions;
                break;
            }
        }

        totalSubmissions += count;

        if (count > 0 && count <= 2) dayCell.style.backgroundColor = '#2d333b';
        else if (count > 2 && count <= 5) dayCell.style.backgroundColor = '#444c56';
        else if (count > 5 && count <= 10) dayCell.style.backgroundColor = '#768390';
        else if (count > 10) dayCell.style.backgroundColor = '#adbac7';

        dayCell.title = `${current.toDateString()}: ${count} submissions`;
        weekCol.appendChild(dayCell);

        // Move to next day
        current.setDate(current.getDate() + 1);
    }

    document.getElementById('totalSubmissions').textContent = `${totalSubmissions} submissions in the last year`;

    // Scroll to the end (Latest activity) automatically
    const graphContainer = document.getElementById('leetcodeGraph');
    if (graphContainer) {
        // We use requestAnimationFrame to ensure the DOM has updated and calculated sizes
        requestAnimationFrame(() => {
            graphContainer.scrollLeft = graphContainer.scrollWidth;
        });
    }
}

// ========================
// Time Counter
// ========================

function startTimeCounter() {
    // Update every second
    setInterval(() => {
        if (window.timeData) {
            const now = Date.now();
            const elapsed = now - window.timeData.birthTimestamp;
            const seconds = Math.floor(elapsed / 1000);
            document.getElementById('timeCounter').textContent = formatNumber(seconds);
        }
    }, 1000);
}

// ========================
// Authentication
// ========================

function openAuthModal() {
    document.getElementById('authModal').classList.remove('hidden');
    resetAuthForm();
}

function toggleAuthMode() {
    state.isSignUpMode = !state.isSignUpMode;
    const title = document.getElementById('authTitle');
    const btnText = document.getElementById('authBtnText');
    const toggleBtn = document.getElementById('toggleAuthMode');
    const usernameField = document.getElementById('usernameField');

    if (state.isSignUpMode) {
        title.textContent = 'Create Account';
        btnText.textContent = 'Sign Up';
        toggleBtn.textContent = 'Already have an account? Sign in';
        usernameField.classList.remove('hidden');
    } else {
        title.textContent = 'Sign In';
        btnText.textContent = 'Sign In';
        toggleBtn.textContent = "Don't have an account? Sign up";
        usernameField.classList.add('hidden');
    }
}

async function handleAuthSubmit(e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const endpoint = state.isSignUpMode ? '/auth/signup' : '/auth/login';

    const payload = {
        email,
        password,
    };

    if (state.isSignUpMode) {
        payload.username = document.getElementById('username').value;
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            throw new Error('Authentication failed');
        }

        const data = await response.json();

        // Store auth data
        localStorage.setItem('authToken', data.token);
        localStorage.setItem('currentUser', JSON.stringify({
            id: data.id,
            email: data.email,
            username: data.username,
        }));

        // Update state
        state.token = data.token;
        state.isLoggedIn = true;
        state.currentUser = data;

        // Update UI
        updateUIForLoggedIn();

        // Close modal
        document.getElementById('authModal').classList.add('hidden');
        resetAuthForm();

        // Show success message
        showNotification('Welcome!', 'You are now signed in.');
    } catch (error) {
        console.error('Authentication error:', error);
        showNotification('Error', 'Authentication failed. Please try again.', 'error');
    }
}

function updateUIForLoggedIn() {
    const authBtn = document.getElementById('authBtn');
    authBtn.textContent = `${state.currentUser.username} (Sign Out)`;
    authBtn.onclick = logout;

    // Show contact form
    document.getElementById('contactForm').classList.remove('hidden');
    document.getElementById('contactPrompt').classList.add('hidden');
}

function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    state.isLoggedIn = false;
    state.currentUser = null;
    state.token = null;

    document.getElementById('authBtn').textContent = 'Sign In';
    document.getElementById('authBtn').onclick = openAuthModal;

    document.getElementById('contactForm').classList.add('hidden');
    document.getElementById('contactPrompt').classList.remove('hidden');
    document.getElementById('successMessage').classList.add('hidden');

    showNotification('Signed Out', 'You have been signed out.');
}

function resetAuthForm() {
    document.getElementById('authForm').reset();
    state.isSignUpMode = false;
    const title = document.getElementById('authTitle');
    const btnText = document.getElementById('authBtnText');
    const toggleBtn = document.getElementById('toggleAuthMode');
    const usernameField = document.getElementById('usernameField');

    title.textContent = 'Sign In';
    btnText.textContent = 'Sign In';
    toggleBtn.textContent = "Don't have an account? Sign up";
    usernameField.classList.add('hidden');
}

function restoreAuthState() {
    if (state.isLoggedIn && state.currentUser) {
        updateUIForLoggedIn();
    }
}

// ========================
// Contact Form
// ========================

async function handleContactSubmit(e) {
    e.preventDefault();

    const subject = document.getElementById('subject').value;
    const message = document.getElementById('message').value;

    try {
        const response = await fetch(`${API_BASE_URL}/contact`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${state.token}`,
            },
            body: JSON.stringify({ subject, message }),
        });

        if (!response.ok) {
            throw new Error('Failed to send message');
        }

        // Show success message
        document.getElementById('messageForm').classList.add('hidden');
        document.getElementById('successMessage').classList.remove('hidden');

        // Reset form after 3 seconds
        setTimeout(() => {
            document.getElementById('messageForm').classList.remove('hidden');
            document.getElementById('successMessage').classList.add('hidden');
            document.getElementById('messageForm').reset();
        }, 3000);

    } catch (error) {
        console.error('Error sending message:', error);
        showNotification('Error', 'Failed to send message. Please try again.', 'error');
    }
}

// ========================
// Component Creation
// ========================

function createProjectCard(project) {
    const card = document.createElement('div');
    card.className = 'card-hover bg-dark-900 border border-subtle rounded-lg overflow-hidden p-6';

    let techStackHTML = '';
    if (project.techStack && project.techStack.length > 0) {
        techStackHTML = `
            <div class="flex flex-wrap gap-2 mb-4">
                ${project.techStack.map(tech => `<span class="text-xs px-2 py-1 rounded bg-dark-800 text-dark-300">${tech}</span>`).join('')}
            </div>
        `;
    }

    let linksHTML = '';
    if (project.githubUrl || project.liveUrl) {
        linksHTML = '<div class="flex gap-4 mt-6 pt-4 border-t border-subtle">';
        if (project.githubUrl) {
            linksHTML += `<a href="${project.githubUrl}" target="_blank" rel="noopener" class="text-dark-300 hover:text-dark-50 text-sm transition">GitHub →</a>`;
        }
        if (project.liveUrl) {
            linksHTML += `<a href="${project.liveUrl}" target="_blank" rel="noopener" class="text-dark-300 hover:text-dark-50 text-sm transition">Live Demo →</a>`;
        }
        linksHTML += '</div>';
    }

    card.innerHTML = `
        <h3 class="text-xl font-semibold mb-2">${project.title}</h3>
        <p class="text-dark-300 text-sm mb-4">${project.shortDescription || project.description}</p>
        ${techStackHTML}
        ${linksHTML}
    `;

    return card;
}

function createSkillPill(skill) {
    const pill = document.createElement('div');
    pill.className = 'flex items-center gap-3 px-6 py-4 rounded-2xl bg-dark-900/30 border border-dark-800/50 hover:bg-dark-800/40 hover:border-dark-700 hover:scale-110 transition-all duration-300 group cursor-default select-none animate-fade-in';

    // Icon mapping using Skill Icons API for high reliability and consistent style
    const iconMap = {
        'java': 'java',
        'python': 'py',
        'c#': 'cs',
        'javascript': 'js',
        'spring boot': 'spring',
        '.net': 'dotnet',
        'react': 'react',
        'azure': 'azure'
    };

    const name = (skill.name || '').trim().toLowerCase();
    const slug = iconMap[name] || 'codesignal';

    pill.innerHTML = `
        <div class="w-8 h-8 flex items-center justify-center rounded-lg bg-dark-950/50 border border-dark-800 p-1.5 transition-transform duration-300">
            <img src="https://skillicons.dev/icons?i=${slug}" 
                 alt="${skill.name}" 
                 class="w-full h-full object-contain">
        </div>
        <span class="text-lg font-display font-bold text-dark-50 tracking-wide">${skill.name}</span>
    `;

    return pill;
}


function formatNumber(num) {
    return new Intl.NumberFormat('en-US').format(num);
}

function showNotification(title, message, type = 'success') {
    // Simple notification - can be enhanced with a proper toast library
    console.log(`[${type.toUpperCase()}] ${title}: ${message}`);
    // You could add a toast notification here
}

// Keyboard shortcuts
document.addEventListener('keydown', (e) => {
    // Escape to close modal
    if (e.key === 'Escape') {
        document.getElementById('authModal').classList.add('hidden');
    }
});
