export interface StockDTO {
  ticker: string;
  companyName: string;
  sector: string;
  price: number;
  volume: number;
  marketCap: number | null;
}
