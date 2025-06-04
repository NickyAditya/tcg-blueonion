// Admin Panel JavaScript

// Global variables for request management
let emailChangeRequests = [];
let notificationSound;

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
        const response = await fetch('/api/email-change-requests/pending');
        const requests = await response.json();
        
        // Check for new requests and notify
        const newRequests = requests.filter(req => 
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

    emailChangeRequests.forEach(request => {
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

    updateRequestCount();
}

// Handle approve/deny actions
async function handleEmailRequest(requestId, status) {
    try {
        const response = await fetch(`/api/email-change-requests/${requestId}/process`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ status })
        });

        if (response.ok) {
            // Refresh the requests list
            loadEmailChangeRequests();
            
            // Show success message
            showToast(`Request ${status.toLowerCase()} successfully`);
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

// Update the pending request count in UI
function updateRequestCount() {
    const pendingCount = emailChangeRequests.filter(r => r.status === 'PENDING').length;
    // You can update a badge or counter in the UI here
    document.title = pendingCount ? `(${pendingCount}) Admin Dashboard` : 'Admin Dashboard';
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
