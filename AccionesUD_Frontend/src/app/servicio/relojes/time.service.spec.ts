import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TimeService } from './time.service';

describe('TimeService', () => {
  let service: TimeService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TimeService]
    });

    service = TestBed.inject(TimeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Verifica que no haya peticiones pendientes
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getTime', () => {
    it('should make HTTP GET request to the correct URL', () => {
      const timezone = 'America/Bogota';
      const mockResponse = {
        abbreviation: "-05",
        client_ip: "181.51.21.240",
        datetime: "2023-11-22T22:28:30.649749-05:00",
        day_of_week: 3,
        day_of_year: 326,
        dst: false,
        dst_from: null,
        dst_offset: 0,
        dst_until: null,
        raw_offset: -18000,
        timezone: "America/Bogota",
        unixtime: 1700707710,
        utc_datetime: "2023-11-23T03:28:30.649749+00:00",
        utc_offset: "-05:00",
        week_number: 47
      };

      service.getTime(timezone).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`https://worldtimeapi.org/api/timezone/${timezone}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should handle different timezones correctly', () => {
      const timezones = ['Europe/London', 'Asia/Tokyo', 'America/New_York'];

      timezones.forEach(timezone => {
        const mockResponse = {
          timezone: timezone,
          datetime: `2023-11-22T12:00:00.000000${timezone === 'Europe/London' ? '+00:00' :
                    timezone === 'Asia/Tokyo' ? '+09:00' : '-05:00'}`,
        };

        service.getTime(timezone).subscribe(response => {
          expect(response.timezone).toBe(timezone);
        });

        const req = httpMock.expectOne(`https://worldtimeapi.org/api/timezone/${timezone}`);
        expect(req.request.method).toBe('GET');
        req.flush(mockResponse);
      });
    });

    it('should handle HTTP error responses', () => {
      const timezone = 'Invalid/Timezone';
      const errorMessage = 'Invalid timezone';

      service.getTime(timezone).subscribe(
        () => fail('should have failed with an error'),
        (error) => {
          expect(error.status).toBe(404);
          expect(error.error).toBe(errorMessage);
        }
      );

      const req = httpMock.expectOne(`https://worldtimeapi.org/api/timezone/${timezone}`);
      expect(req.request.method).toBe('GET');
      req.flush(errorMessage, { status: 404, statusText: 'Not Found' });
    });

    it('should handle network errors', () => {
      const timezone = 'America/Bogota';
      const errorEvent = new ErrorEvent('Network error', {
        message: 'Failed to connect to the API'
      });

      service.getTime(timezone).subscribe(
        () => fail('should have failed with a network error'),
        (error) => {
          expect(error).toBeTruthy();
        }
      );

      const req = httpMock.expectOne(`https://worldtimeapi.org/api/timezone/${timezone}`);
      req.error(errorEvent);
    });

    it('should preserve query parameters if provided', () => {
      const timezone = 'America/Bogota';
      const mockResponse = { timezone: timezone, datetime: '2023-11-22T22:28:30.649749-05:00' };

      // Suponiendo que en algún momento se podría querer pasar parámetros
      service.getTime(timezone).subscribe();

      const req = httpMock.expectOne(`https://worldtimeapi.org/api/timezone/${timezone}`);
      expect(req.request.method).toBe('GET');
      expect(req.request.url).toBe(`https://worldtimeapi.org/api/timezone/${timezone}`);
      req.flush(mockResponse);
    });

    it('should return the full response object with all expected properties', () => {
      const timezone = 'America/Bogota';
      const mockResponse = {
        abbreviation: "-05",
        client_ip: "181.51.21.240",
        datetime: "2023-11-22T22:28:30.649749-05:00",
        day_of_week: 3,
        day_of_year: 326,
        dst: false,
        dst_from: null,
        dst_offset: 0,
        dst_until: null,
        raw_offset: -18000,
        timezone: "America/Bogota",
        unixtime: 1700707710,
        utc_datetime: "2023-11-23T03:28:30.649749+00:00",
        utc_offset: "-05:00",
        week_number: 47
      };

      service.getTime(timezone).subscribe(response => {
        expect(response).toBeDefined();
        expect(response.abbreviation).toBe("-05");
        expect(response.datetime).toBeDefined();
        expect(response.timezone).toBe("America/Bogota");
        expect(response.utc_offset).toBe("-05:00");
        expect(response.unixtime).toBeDefined();
      });

      const req = httpMock.expectOne(`https://worldtimeapi.org/api/timezone/${timezone}`);
      req.flush(mockResponse);
    });
  });
});
