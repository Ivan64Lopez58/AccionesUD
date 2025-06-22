import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private renderer: Renderer2;
  private _darkMode = new BehaviorSubject<boolean>(false);
  darkMode$ = this._darkMode.asObservable();

  constructor(rendererFactory: RendererFactory2) {
    this.renderer = rendererFactory.createRenderer(null, null);
    this.initTheme();
  }

  private initTheme(): void {
    const savedTheme = localStorage.getItem('userTheme');
    const isDark = savedTheme === 'dark';
    this._darkMode.next(isDark);
    this.applyTheme(isDark);
  }

  setDarkMode(isDark: boolean): void {
    this._darkMode.next(isDark);
    const theme = isDark ? 'dark' : 'light';
    localStorage.setItem('userTheme', theme);
    this.applyTheme(isDark);
  }

  toggleDarkMode(): void {
    const isDark = !this._darkMode.value;
    this.setDarkMode(isDark);
  }

  private applyTheme(isDark: boolean): void {
    if (isDark) {
      document.body.classList.add('dark-theme');
    } else {
      document.body.classList.remove('dark-theme');
    }
  }
}
