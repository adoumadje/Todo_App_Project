import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TodoListerComponent } from './todo-lister.component';

describe('TodoListerComponent', () => {
  let component: TodoListerComponent;
  let fixture: ComponentFixture<TodoListerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TodoListerComponent]
    });
    fixture = TestBed.createComponent(TodoListerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
