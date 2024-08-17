import { TestBed } from '@angular/core/testing';

import { CardAssignmentService } from './card-assignment.service';

describe('CardAssignmentService', () => {
  let service: CardAssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CardAssignmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
