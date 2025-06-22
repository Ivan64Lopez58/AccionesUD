import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { CandlestickSeries, createChart, IChartApi, ISeriesApi } from 'lightweight-charts';
import { AccionHistorica } from '../servicio/acciones/order.service';
import { OrderService } from '../servicio/acciones/order.service';

@Component({
  selector: 'app-grafica',
  templateUrl: './grafica.component.html',
  styleUrls: ['./grafica.component.css']
})
export class GraficaComponent implements OnInit, AfterViewInit {

  @ViewChild('chartContainer', { static: false }) chartContainer!: ElementRef;

  @Input() chartInicialData!: AccionHistorica[];

  @Input() width: number = 600;
  @Input() height: number = 300;

  private chart!: IChartApi;
  private series!: ISeriesApi<'Candlestick'>;

  constructor() { }

  ngOnInit() {

  }

  ngAfterViewInit() {
    this.initializeChart();
    this.series.setData(this.chartInicialData)

  }

  initializeChart() {
    const chartOptions = { 
      width: this.width,
      height: this.height,
      layout: { 
        background: 
        { 
          color: 'white' 
        },
        textColor: '#000000'
      },
      rightPriceScale: { 
        borderColor: '#cccccc',
      },
      grid: {
        vertLines: { color: '#cdcdcd' },
        horzLines: { color: '#cdcdcd' },
      },
      timeScale: {
        borderColor: '#cccccc',
        timeVisible: true,
        secondsVisible: false,
      }, 
    };
    this.chart = createChart(this.chartContainer.nativeElement,chartOptions)
    this.series = this.chart.addSeries(CandlestickSeries)
  }


}
