export interface StockDTO {
  ticker: string;
  name: string;
  exchange: string;
  price: number;
  volume: number;
  date: string | null;
}
