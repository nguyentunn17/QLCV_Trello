import { animate } from '@angular/animations';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'trello-2024';

  activeSignIn():void {
    const wrapper= document.querySelector ('.wrapper');
    if (wrapper) {
    wrapper.classList.add('activeSignIn');
    wrapper.classList.remove('activeSignUp');
    }
  }
  activeSignUp():void {
    const wrapper= document.querySelector ('.wrapper');
    if (wrapper) {
      wrapper.classList.add('activeSignUp');
      wrapper.classList.remove('activeSignIn');
    }
  }
  changeColor(color: string) {
    const bgAnimate = document.getElementById('bg-animate');
    if (bgAnimate){
    bgAnimate.style.background = color;
    bgAnimate.classList.add('active');
    
    setTimeout(()=>{
      bgAnimate.classList.remove('active');
    }, 1200); 
  }
}
  
}

