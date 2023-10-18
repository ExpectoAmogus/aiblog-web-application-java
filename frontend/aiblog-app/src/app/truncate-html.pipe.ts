import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'truncateHTML'
})
export class TruncateHTMLPipe implements PipeTransform {

  transform(html: string, maxLength: number): string {
    let truncatedText = html.slice(0, maxLength);
    if (html.length > maxLength) {
      truncatedText += '...';
    }
    return truncatedText;
  }

}
