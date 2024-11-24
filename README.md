
# Live Betting API

Bu proje, canlı bahis uygulaması olup, kullanıcıların maçları ve kuponları yönetmesine olanak tanır.

## İçindekiler
- [Test Kapsamı](#test-kapsamı)
- [Bağımlılıklar](#bağımlılıklar)
- [Swagger](#swagger)
- [API Endpoints](#api-endpoints)
  - [Maç Endpoints](#maç-endpoints)
  - [Kupon Endpoints](#kupon-endpoints)
- [Örnek API İstekleri](#örnek-api-istekleri)

## Test Kapsamı
- **Test Coverage:** %79 (Integration & Unit Testler)

## Bağımlılıklar
- **Veritabanı:** H2 (In-Memory Database)
- **Nesne Dönüşümü:** MapStruct
- **API Dokümantasyonu:** OpenAPI Swagger

## Swagger
- Swagger dokümantasyonu şu adreste mevcut: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## API Endpoints

### Maç Endpoints
1. **Maç Ekle**
   - `POST /api/v1/match`
   - Açıklama: Yeni bir maç ekler.

2. **Tüm Maçları Getir**
   - `GET /api/v1/match`
   - Açıklama: Sayfalı bir şekilde tüm maçları listeler.

3. **Maç Sil**
   - `DELETE /api/v1/match/{id}`
   - Açıklama: Verilen ID ile bir maçı siler.

4. **Maç Oran Geçmişini Getir**
   - `GET /api/v1/match/{matchId}/odds-history`
   - Açıklama: Verilen maç ID’si için oran geçmişini getirir.

### Kupon Endpoints
1. **Kupon Ekle**
   - `POST /api/v1/coupon`
   - Açıklama: Yeni bir kupon ekler.

2. **Tüm Kuponları Getir**
   - `GET /api/v1/coupon`
   - Açıklama: Sayfalı bir şekilde tüm kuponları listeler.

3. **Kupon Sil**
   - `DELETE /api/v1/coupon?id={id}`
   - Açıklama: Verilen ID ile bir kuponu siler.

---

## Örnek API İstekleri

### Maç Ekle

**İstek:**
```bash
POST /api/v1/match
Content-Type: application/json

{
  "homeTeam": "Team A",
  "awayTeam": "Team B",
  "startTime": "2024-12-01T15:00:00",
  "currentHomeWinOdds": 2.5,
  "currentDrawOdds": 3.1,
  "currentAwayWinOdds": 2.8
}
```

**Cevap:**
```json
{
  "id": 1,
  "homeTeam": "Team A",
  "awayTeam": "Team B",
  "startTime": "2024-12-01T15:00:00",
  "currentHomeWinOdds": 2.5,
  "currentDrawOdds": 3.1,
  "currentAwayWinOdds": 2.8
}
```

### Tüm Maçları Getir

**İstek:**
```bash
GET /api/v1/match?page=0&pageSize=10
```

**Cevap:**
```json
{
  "content": [
    {
      "id": 1,
      "homeTeam": "Team A",
      "awayTeam": "Team B",
      "startTime": "2024-12-01T15:00:00",
      "currentHomeWinOdds": 2.5,
      "currentDrawOdds": 3.1,
      "currentAwayWinOdds": 2.8
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### Maç Sil

**İstek:**
```bash
DELETE /api/v1/match/1
```

**Cevap:**
```bash
HTTP/1.1 200 OK
```

### Maç Oran Geçmişini Getir

**İstek:**
```bash
GET /api/v1/match/1/odds-history?page=0&pageSize=10
```

**Cevap:**
```json
{
  "matchId": 1,
  "oddsHistory": [
    {
      "homeWinOdds": 2.5,
      "drawOdds": 3.1,
      "awayWinOdds": 2.8,
      "timestamp": "2024-12-01T14:00:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### Kupon Ekle

**İstek:**
```bash
POST /api/v1/coupon
Content-Type: application/json

{
  "price": 50.00,
  "selectedBets": [
    {
      "matchId": 1,
      "betType": "HOME"
    }
  ]
}
```

**Cevap:**
```json
{
  "couponId": "1",
  "price": 50.00,
  "selectedBet": [
    {
      "matchId": 1,
      "selectedBet": "HOME",
      "selectedOdds": 2.5
    }
  ]
}
```

### Tüm Kuponları Getir

**İstek:**
```bash
GET /api/v1/coupon?page=0&pageSize=10
```

**Cevap:**
```json
{
  "content": [
    {
      "couponId": "1",
      "price": 50.00,
      "selectedBet": [
        {
          "matchId": 1,
          "selectedBet": "HOME",
          "selectedOdds": 2.5
        }
      ]
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### Kupon Sil

**İstek:**
```bash
DELETE /api/v1/coupon?id=1
```

**Cevap:**
```bash
HTTP/1.1 200 OK
```

---

