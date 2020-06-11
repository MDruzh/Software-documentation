import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { NetflixSharedModule } from 'app/shared/shared.module';
import { NetflixCoreModule } from 'app/core/core.module';
import { NetflixAppRoutingModule } from './app-routing.module';
import { NetflixHomeModule } from './home/home.module';
import { NetflixEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    NetflixSharedModule,
    NetflixCoreModule,
    NetflixHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    NetflixEntityModule,
    NetflixAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class NetflixAppModule {}
