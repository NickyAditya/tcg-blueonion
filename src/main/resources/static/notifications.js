// Notification system
class NotificationSystem {
    constructor() {
        this.container = document.getElementById('notificationContainer');
        this.history = document.getElementById('notificationHistory');
        this.historyList = document.getElementById('notificationHistoryList');
        this.badge = document.getElementById('notificationBadge');
        this.clearButton = document.getElementById('clearNotifications');
        this.bellIcon = document.getElementById('notificationLog');
        this.notifications = [];
        this.unreadCount = 0;

        // Initialize event listeners
        if (this.bellIcon) {
            this.bellIcon.addEventListener('click', () => this.toggleHistory());
        }
        if (this.clearButton) {
            this.clearButton.addEventListener('click', () => this.clearHistory());
        }

        // Close history panel when clicking outside
        if (this.history) {
            document.addEventListener('click', (e) => {
                if (!this.history.contains(e.target) && 
                    !this.bellIcon.contains(e.target) && 
                    this.history.classList.contains('show')) {
                    this.toggleHistory();
                }
            });
        }

        // Load notifications from localStorage
        this.loadNotifications();
    }    show(title, message, type = 'info', duration = 5000) {
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        
        notification.innerHTML = `
            <div class="notification-content">
                <div class="notification-title">${title}</div>
                <div class="notification-message">${message}</div>
            </div>
            <button class="notification-close" aria-label="Close notification">&times;</button>
        `;

        // Add to container
        this.container.appendChild(notification);
        
        // Force a reflow to enable the transition
        notification.offsetHeight;
        
        // Show notification
        notification.classList.add('show');
        
        // Add to history
        this.addToHistory(title, message, type);

        // Add close button functionality
        const closeBtn = notification.querySelector('.notification-close');
        closeBtn.addEventListener('click', () => this.remove(notification));

        // Auto remove after duration
        if (duration > 0) {
            setTimeout(() => this.remove(notification), duration);
        }

        return notification;
    }

    remove(notification) {
        notification.style.animation = 'slideOut 0.3s ease-out forwards';
        setTimeout(() => {
            if (notification.parentElement === this.container) {
                this.container.removeChild(notification);
            }
        }, 300);
    }

    addToHistory(title, message, type) {
        const timestamp = new Date().toLocaleString();
        const item = document.createElement('li');
        item.className = 'notification-history-item';
        item.innerHTML = `
            <div class="notification-content">
                <div class="notification-title">${title}</div>
                <div class="notification-message">${message}</div>
                <small class="notification-timestamp">${timestamp}</small>
            </div>
        `;
        
        if (this.historyList) {
            this.historyList.insertBefore(item, this.historyList.firstChild);
        }
        
        this.notifications.unshift({ title, message, type, timestamp });
        this.updateBadge(++this.unreadCount);
        this.saveNotifications();
    }

    toggleHistory() {
        if (!this.history) return;
        this.history.classList.toggle('show');
        if (this.history.classList.contains('show')) {
            this.unreadCount = 0;
            this.updateBadge(0);
            this.saveNotifications();
        }
    }

    clearHistory() {
        if (this.historyList) {
            this.historyList.innerHTML = '';
        }
        this.notifications = [];
        this.unreadCount = 0;
        this.updateBadge(0);
        this.saveNotifications();
        
        if (this.history) {
            this.history.style.display = 'none';
        }
    }

    updateBadge(count) {
        if (this.badge) {
            this.badge.textContent = count || '';
        }
    }

    loadNotifications() {
        const saved = localStorage.getItem('notifications');
        if (saved) {
            const data = JSON.parse(saved);
            this.notifications = data.items || [];
            this.unreadCount = data.unreadCount || 0;
            
            if (this.historyList) {
                // Rebuild notification history
                this.notifications.forEach(({ title, message, timestamp }) => {
                    const item = document.createElement('li');
                    item.className = 'notification-history-item';
                    item.innerHTML = `
                        <div class="notification-content">
                            <div class="notification-title">${title}</div>
                            <div class="notification-message">${message}</div>
                            <small class="notification-timestamp">${timestamp}</small>
                        </div>
                    `;
                    this.historyList.appendChild(item);
                });
            }
            
            this.updateBadge(this.unreadCount);
        }
    }

    saveNotifications() {
        localStorage.setItem('notifications', JSON.stringify({
            items: this.notifications,
            unreadCount: this.unreadCount
        }));
    }

    // Convenience methods for different notification types
    success(title, message, duration) {
        return this.show(title, message, 'success', duration);
    }

    error(title, message, duration) {
        return this.show(title, message, 'error', duration);
    }

    info(title, message, duration) {
        return this.show(title, message, 'info', duration);
    }

    warning(title, message, duration) {
        return this.show(title, message, 'warning', duration);
    }
}

// Initialize notification system as a global variable
window.notifications = new NotificationSystem();
