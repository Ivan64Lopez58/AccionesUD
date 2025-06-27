import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { DebugElement } from '@angular/core';

import { PiePaginaPrincipalComponent } from './pie-pagina-principal.component';

describe('PiePaginaPrincipalComponent', () => {
  let component: PiePaginaPrincipalComponent;
  let fixture: ComponentFixture<PiePaginaPrincipalComponent>;
  let debugElement: DebugElement;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        PiePaginaPrincipalComponent,
        TranslateModule.forRoot()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PiePaginaPrincipalComponent);
    component = fixture.componentInstance;
    debugElement = fixture.debugElement;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a footer element', () => {
    const footerElement = debugElement.query(By.css('footer'));
    expect(footerElement).toBeTruthy();
  });

  it('should display the logo', () => {
    const logoElement = debugElement.query(By.css('.footer-logo'));
    expect(logoElement).toBeTruthy();
  });

  it('should have 5 social media links', () => {
    const socialLinks = debugElement.queryAll(By.css('.social-icons a'));
    expect(socialLinks.length).toBe(5);
  });

  it('should have 4 footer sections with links', () => {
    const linkSections = debugElement.queryAll(By.css('.footer-section.links'));
    expect(linkSections.length).toBe(4);
  });

  it('should have correct section titles', () => {
    const sectionTitles = debugElement.queryAll(By.css('.footer-section.links h2'));
    expect(sectionTitles.length).toBe(4);

    // No podemos comparar el texto directamente debido a las traducciones
    // pero podemos verificar que existan los elementos
    expect(sectionTitles[0]).toBeTruthy();
    expect(sectionTitles[1]).toBeTruthy();
    expect(sectionTitles[2]).toBeTruthy();
    expect(sectionTitles[3]).toBeTruthy();
  });

  describe('Modal functionality', () => {
    it('modal should be initially hidden', () => {
      const modalElement = debugElement.query(By.css('.modal-overlay'));
      expect(modalElement).toBeNull();
    });

    it('should open empresa modal when company link is clicked', () => {
      // Encontrar el enlace de "La empresa" - está en el segundo .footer-section.links
      const linkSections = debugElement.queryAll(By.css('.footer-section.links'));
      const companyLink = linkSections[1].queryAll(By.css('li a'))[0];

      companyLink.triggerEventHandler('click', null);
      fixture.detectChanges();

      expect(component.modalVisible).toBeTrue();
      expect(component.modalType).toBe('empresa');

      const modalElement = debugElement.query(By.css('.modal-overlay'));
      expect(modalElement).toBeTruthy();
    });

    it('should open contacto modal when contact link is clicked', () => {
      // Encontrar el enlace de "Contacto" - está en el segundo .footer-section.links
      const linkSections = debugElement.queryAll(By.css('.footer-section.links'));
      const contactLink = linkSections[1].queryAll(By.css('li a'))[1];

      contactLink.triggerEventHandler('click', null);
      fixture.detectChanges();

      expect(component.modalVisible).toBeTrue();
      expect(component.modalType).toBe('contacto');

      const modalElement = debugElement.query(By.css('.modal-overlay'));
      expect(modalElement).toBeTruthy();
    });

    it('should open documentacion modal when documentation link is clicked', () => {
      // Encontrar el enlace de "Documentación" - está en el cuarto .footer-section.links
      const linkSections = debugElement.queryAll(By.css('.footer-section.links'));
      const docLink = linkSections[3].queryAll(By.css('li a'))[0];

      docLink.triggerEventHandler('click', null);
      fixture.detectChanges();

      expect(component.modalVisible).toBeTrue();
      expect(component.modalType).toBe('documentacion');

      const modalElement = debugElement.query(By.css('.modal-overlay'));
      expect(modalElement).toBeTruthy();
    });

    it('should open aviso legal modal when legal notice link is clicked', () => {
      // Encontrar el enlace de "Aviso Legal" - está en el cuarto .footer-section.links
      const linkSections = debugElement.queryAll(By.css('.footer-section.links'));
      const legalLink = linkSections[3].queryAll(By.css('li a'))[1];

      legalLink.triggerEventHandler('click', null);
      fixture.detectChanges();

      expect(component.modalVisible).toBeTrue();
      expect(component.modalType).toBe('avisoLegal');

      const modalElement = debugElement.query(By.css('.modal-overlay'));
      expect(modalElement).toBeTruthy();
    });

    it('should open privacidad modal when privacy policy link is clicked', () => {
      // Encontrar el enlace de "Política de Privacidad" - está en el cuarto .footer-section.links
      const linkSections = debugElement.queryAll(By.css('.footer-section.links'));
      const privacyLink = linkSections[3].queryAll(By.css('li a'))[2];

      privacyLink.triggerEventHandler('click', null);
      fixture.detectChanges();

      expect(component.modalVisible).toBeTrue();
      expect(component.modalType).toBe('privacidad');

      const modalElement = debugElement.query(By.css('.modal-overlay'));
      expect(modalElement).toBeTruthy();
    });

    it('should close modal when clicking the close button', () => {
      // Primero abrimos el modal
      component.openModal('empresa');
      fixture.detectChanges();

      // Verificamos que esté abierto
      let modalElement = debugElement.query(By.css('.modal-overlay'));
      expect(modalElement).toBeTruthy();

      // Hacemos clic en el botón de cerrar
      const closeButton = debugElement.query(By.css('.close-btn'));
      closeButton.triggerEventHandler('click', null);
      fixture.detectChanges();

      // Verificamos que el modal se haya cerrado
      expect(component.modalVisible).toBeFalse();
      expect(component.modalType).toBeNull();

      modalElement = debugElement.query(By.css('.modal-overlay'));
      expect(modalElement).toBeNull();
    });    it('should show the correct content when empresa modal is opened', () => {
      component.openModal('empresa');
      fixture.detectChanges();

      // Cuando el modal está abierto, verificamos directamente el contenido de la modal
      const modalOverlay = debugElement.query(By.css('.modal-overlay'));
      expect(modalOverlay).toBeTruthy();

      // Buscar los elementos h2 y h3 dentro del modal cuando modalType es 'empresa'
      const h2Elements = modalOverlay.queryAll(By.css('h2'));
      const h3Elements = modalOverlay.queryAll(By.css('h3'));

      expect(h2Elements.length).toBeGreaterThan(0);
      expect(h3Elements.length).toBe(3); // Misión, Visión y Calidad
    });

    it('should show the correct content when contacto modal is opened', () => {
      component.openModal('contacto');
      fixture.detectChanges();

      // Cuando el modal está abierto, verificamos directamente el contenido de la modal
      const modalOverlay = debugElement.query(By.css('.modal-overlay'));
      expect(modalOverlay).toBeTruthy();

      // Verificar que haya contenido de contacto
      const h2Element = modalOverlay.query(By.css('h2'));
      const pElement = modalOverlay.query(By.css('p'));

      expect(h2Element).toBeTruthy();
      expect(pElement).toBeTruthy();
    });

    it('should show the correct content when documentacion modal is opened', () => {
      component.openModal('documentacion');
      fixture.detectChanges();

      // Cuando el modal está abierto, verificamos directamente el contenido de la modal
      const modalOverlay = debugElement.query(By.css('.modal-overlay'));
      expect(modalOverlay).toBeTruthy();

      // Verificar que haya contenido de documentación
      const h2Element = modalOverlay.query(By.css('h2'));
      const pElements = modalOverlay.queryAll(By.css('p'));

      expect(h2Element).toBeTruthy();
      expect(pElements.length).toBe(3); // 3 párrafos de documentación
    });
  });

  describe('Component methods', () => {
    it('openModal method should set modalType and make modal visible', () => {
      component.openModal('empresa');

      expect(component.modalType).toBe('empresa');
      expect(component.modalVisible).toBeTrue();
    });

    it('closeModal method should reset modalType and hide modal', () => {
      // Primero establecemos el estado como si el modal estuviera abierto
      component.modalType = 'empresa';
      component.modalVisible = true;

      // Llamamos al método para cerrar
      component.closeModal();

      expect(component.modalType).toBeNull();
      expect(component.modalVisible).toBeFalse();
    });
  });
});
