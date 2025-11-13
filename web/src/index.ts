/**
 * flam-edge Web Viewer
 * TypeScript-based viewer for displaying processed frames from Android app
 */

interface FrameStats {
    fps: number;
    width: number;
    height: number;
    processingTime: number;
    filterType: string;
}

class FrameViewer {
    private frameImage: HTMLImageElement;
    private fpsValue: HTMLElement;
    private resolutionValue: HTMLElement;
    private processingTime: HTMLElement;
    private filterType: HTMLElement;
    private fileInput: HTMLInputElement;
    private generateSampleBtn: HTMLButtonElement;
    private clearBtn: HTMLButtonElement;

    private currentStats: FrameStats = {
        fps: 0,
        width: 640,
        height: 480,
        processingTime: 0,
        filterType: 'Canny Edge'
    };

    constructor() {
        this.initializeElements();
        this.attachEventListeners();
        this.updateStats();
        console.log('🔥 flam-edge Web Viewer initialized');
    }

    private initializeElements(): void {
        this.frameImage = document.getElementById('frameImage') as HTMLImageElement;
        this.fpsValue = document.getElementById('fpsValue') as HTMLElement;
        this.resolutionValue = document.getElementById('resolutionValue') as HTMLElement;
        this.processingTime = document.getElementById('processingTime') as HTMLElement;
        this.filterType = document.getElementById('filterType') as HTMLElement;
        this.fileInput = document.getElementById('fileInput') as HTMLInputElement;
        this.generateSampleBtn = document.getElementById('generateSampleBtn') as HTMLButtonElement;
        this.clearBtn = document.getElementById('clearBtn') as HTMLButtonElement;
    }

    private attachEventListeners(): void {
        this.fileInput.addEventListener('change', (e) => this.handleFileUpload(e));
        this.generateSampleBtn.addEventListener('click', () => this.generateSampleImage());
        this.clearBtn.addEventListener('click', () => this.clearDisplay());
    }

    private handleFileUpload(event: Event): void {
        const target = event.target as HTMLInputElement;
        const file = target.files?.[0];

        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();

            reader.onload = (e) => {
                const result = e.target?.result as string;
                this.displayImage(result);
                this.updateStatsFromImage(file);
            };

            reader.readAsDataURL(file);
        }
    }

    private displayImage(imageData: string): void {
        this.frameImage.src = imageData;
        console.log('✅ Image loaded successfully');
    }

    private updateStatsFromImage(file: File): void {
        const img = new Image();
        img.onload = () => {
            this.currentStats.width = img.width;
            this.currentStats.height = img.height;
            this.currentStats.processingTime = Math.floor(Math.random() * 50) + 10; // Simulated
            this.currentStats.fps = 15.0;
            this.updateStats();
        };
        img.src = URL.createObjectURL(file);
    }

    private generateSampleImage(): void {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');

        if (!ctx) return;

        canvas.width = 640;
        canvas.height = 480;

        // Create sample edge-detected pattern
        ctx.fillStyle = '#000000';
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        ctx.strokeStyle = '#FFFFFF';
        ctx.lineWidth = 2;

        // Draw some edge-like patterns
        for (let i = 0; i < 50; i++) {
            ctx.beginPath();
            const x1 = Math.random() * canvas.width;
            const y1 = Math.random() * canvas.height;
            const x2 = x1 + (Math.random() * 100 - 50);
            const y2 = y1 + (Math.random() * 100 - 50);
            ctx.moveTo(x1, y1);
            ctx.lineTo(x2, y2);
            ctx.stroke();
        }

        // Draw some geometric shapes (edges)
        ctx.strokeStyle = '#FFFFFF';
        ctx.lineWidth = 1;

        // Rectangle
        ctx.strokeRect(50, 50, 200, 150);

        // Circle
        ctx.beginPath();
        ctx.arc(450, 250, 80, 0, 2 * Math.PI);
        ctx.stroke();

        // Diagonal lines
        for (let i = 0; i < 10; i++) {
            ctx.beginPath();
            ctx.moveTo(i * 64, 0);
            ctx.lineTo(i * 64, 480);
            ctx.stroke();
        }

        const imageData = canvas.toDataURL('image/png');
        this.displayImage(imageData);

        this.currentStats = {
            fps: 15.0,
            width: 640,
            height: 480,
            processingTime: 25,
            filterType: 'Canny Edge'
        };

        this.updateStats();
        console.log('🎲 Sample image generated');
    }

    private clearDisplay(): void {
        this.frameImage.src = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==';

        this.currentStats = {
            fps: 0,
            width: 0,
            height: 0,
            processingTime: 0,
            filterType: 'None'
        };

        this.updateStats();
        console.log('🗑️ Display cleared');
    }

    private updateStats(): void {
        this.fpsValue.textContent = `${this.currentStats.fps.toFixed(1)} FPS`;
        this.resolutionValue.textContent = `${this.currentStats.width} x ${this.currentStats.height}`;
        this.processingTime.textContent = `${this.currentStats.processingTime} ms`;
        this.filterType.textContent = this.currentStats.filterType;
    }

    /**
     * Update frame from external source (e.g., WebSocket, HTTP)
     * This method can be called when receiving frames from Android app
     */
    public updateFrame(base64Image: string, stats: Partial<FrameStats>): void {
        this.displayImage(`data:image/png;base64,${base64Image}`);
        this.currentStats = { ...this.currentStats, ...stats };
        this.updateStats();
    }

    /**
     * Get current frame statistics
     */
    public getStats(): FrameStats {
        return { ...this.currentStats };
    }
}

// Initialize the viewer when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    const viewer = new FrameViewer();

    // Expose viewer to global scope for external access (optional)
    (window as any).frameViewer = viewer;

    console.log('🚀 Web viewer ready');
});

// Export for module usage
export { FrameViewer, FrameStats };