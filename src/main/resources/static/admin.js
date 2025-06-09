// Admin Panel JavaScript

// Global variables for request management
let emailChangeRequests = [];
let notificationSound;
let currentPage = 1;
const itemsPerPage = 5;
let currentFilter = 'all';

// Initialize when document is ready
document.addEventListener('DOMContentLoaded', () => {
    initializeNotifications();
    loadEmailChangeRequests();
    // Poll for new requests every minute
    setInterval(loadEmailChangeRequests, 60000);
});

// Initialize notification sound and permission
async function initializeNotifications() {
    notificationSound = new Audio('/notification.mp3');
    if (Notification.permission !== "granted") {
        await Notification.requestPermission();
    }
}

// Load email change requests from server
async function loadEmailChangeRequests() {
    try {
        const response = await fetch('/api/email-change/all'); // Changed to fetch all requests
        const requests = await response.json();
        
        // Check for new pending requests and notify
        const newRequests = requests.filter(req => 
            req.status === 'PENDING' && 
            !emailChangeRequests.find(existing => existing.id === req.id)
        );
        
        if (newRequests.length > 0) {
            notifyNewRequests(newRequests);
        }
        
        emailChangeRequests = requests;
        renderEmailRequests();
    } catch (error) {
        console.error('Error loading email change requests:', error);
    }
}

// Render email requests in the table
function renderEmailRequests() {
    const tbody = document.getElementById('emailRequestsTableBody');
    tbody.innerHTML = '';

    const totalPages = Math.ceil(emailChangeRequests.length / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = Math.min(startIndex + itemsPerPage, emailChangeRequests.length);    // Filter requests based on selected status
    let filteredRequests = emailChangeRequests;
    if (currentFilter !== 'all') {
        filteredRequests = emailChangeRequests.filter(r => r.status === currentFilter);
    }

    // Sort requests to show PENDING first, then recent APPROVED/DENIED
    const sortedRequests = [...filteredRequests].sort((a, b) => {
        // If showing all requests, prioritize PENDING
        if (currentFilter === 'all') {
            const statusPriority = {
                'PENDING': 0,
                'APPROVED': 1,
                'DENIED': 1
            };
            
            const aStatus = statusPriority[a.status] ?? 2;
            const bStatus = statusPriority[b.status] ?? 2;
            
            if (aStatus !== bStatus) {
                return aStatus - bStatus;
            }
        }
        
        // Sort by date, newest first
        return new Date(b.requestDate) - new Date(a.requestDate);
    });

    // Render the current page's requests
    sortedRequests.slice(startIndex, endIndex).forEach(request => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${request.username}</td>
            <td>${request.currentEmail}</td>
            <td>${request.requestedEmail}</td>
            <td>${new Date(request.requestDate).toLocaleString()}</td>
            <td><span class="request-status status-${request.status.toLowerCase()}">${request.status}</span></td>
            <td class="request-actions">
                ${request.status === 'PENDING' ? `
                    <button class="approve-btn" onclick="handleEmailRequest(${request.id}, 'APPROVED')">Approve</button>
                    <button class="deny-btn" onclick="handleEmailRequest(${request.id}, 'DENIED')">Deny</button>
                ` : ''}
            </td>
        `;
        tbody.appendChild(tr);
    });

    renderPagination(totalPages);
    updateRequestCount();
}

// Handle approve/deny actions
async function handleEmailRequest(requestId, status) {
    try {
        const endpoint = status === 'APPROVED' ? 'approve' : 'deny';
        const response = await fetch(`/api/email-change/${requestId}/${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            // Find the request in our list and update its status
            const request = emailChangeRequests.find(r => r.id === requestId);
            if (request) {
                request.status = status;
                request.processedDate = new Date();
            }

            // Re-render with the updated status
            renderEmailRequests();
            
            // Show success message
            showToast(`Request ${status.toLowerCase()} successfully`);
            
            // Refresh the full list after a short delay to ensure user sees the status change
            setTimeout(loadEmailChangeRequests, 2000);
        } else {
            throw new Error('Failed to process request');
        }
    } catch (error) {
        console.error('Error processing request:', error);
        showToast('Error processing request', 'error');
    }
}

// Show notifications for new requests
function notifyNewRequests(newRequests) {
    if (!newRequests.length) return;

    // Play notification sound
    notificationSound.play().catch(console.error);

    // Show desktop notification if permitted
    if (Notification.permission === "granted") {
        new Notification("New Email Change Requests", {
            body: `${newRequests.length} new email change request${newRequests.length > 1 ? 's' : ''}`,
            icon: "/favicon.ico"
        });
    }

    // Update the UI notification count
    updateRequestCount();
}

// Render pagination controls
function renderPagination(totalPages) {
    const paginationContainer = document.getElementById('paginationContainer');
    if (!paginationContainer) return;

    paginationContainer.innerHTML = '';
    
    if (totalPages <= 1) return;

    function addPageButton(pageNum, text = pageNum, isCurrent = false) {
        const button = document.createElement('button');
        button.textContent = text;
        button.className = `page-button ${isCurrent ? 'current-page' : ''}`;
        if (!isCurrent) {
            button.onclick = () => {
                currentPage = pageNum;
                renderEmailRequests();
            };
        }
        paginationContainer.appendChild(button);
    }

    // First page
    addPageButton(1);

    if (totalPages <= 7) {
        // Show all pages if 7 or fewer
        for (let i = 2; i < totalPages; i++) {
            addPageButton(i, i, i === currentPage);
        }
    } else {
        if (currentPage <= 4) {
            // Near start
            for (let i = 2; i <= 5; i++) {
                addPageButton(i, i, i === currentPage);
            }
            addPageButton(null, '...');
        } else if (currentPage >= totalPages - 3) {
            // Near end
            addPageButton(null, '...');
            for (let i = totalPages - 4; i < totalPages; i++) {
                addPageButton(i, i, i === currentPage);
            }
        } else {
            // Middle
            addPageButton(null, '...');
            for (let i = currentPage - 1; i <= currentPage + 1; i++) {
                addPageButton(i, i, i === currentPage);
            }
            addPageButton(null, '...');
        }
    }

    // Last page
    if (totalPages > 1) {
        addPageButton(totalPages);
    }
}

// Update request count
function updateRequestCount() {
    const pendingCount = emailChangeRequests.filter(r => r.status === 'PENDING').length;
    // You can update a badge or counter in the UI here
    document.title = pendingCount ? `(${pendingCount}) Admin Dashboard` : 'Admin Dashboard';
}

// Filter requests by status
function filterRequests() {
    const select = document.getElementById('statusFilter');
    currentFilter = select.value;
    currentPage = 1; // Reset to first page when filter changes
    renderEmailRequests();
}

// Show toast messages
function showToast(message, type = 'success') {
    // Assuming you have a toast component or want to implement one
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.remove();
    }, 3000);
}
