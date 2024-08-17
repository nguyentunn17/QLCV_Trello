import { TestBed } from '@angular/core/testing';

import { CardLabelService } from './card-label.service';

describe('CardLabelService', () => {
  let service: CardLabelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CardLabelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
