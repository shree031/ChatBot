import {Component} from '@angular/core';
import {ChatService} from "../services/ChatService";
import {IonicModule} from "@ionic/angular";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {MarkdownComponent} from "ngx-markdown";

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrl: 'home.page.scss',
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule, MarkdownComponent]
})
export class HomePage {
  messages: { sender: string; text: string }[] = [];
  newMessage = '';
  loading = false; // typing indicator flag

  constructor(private chatService: ChatService) {}

  sendMessage() {
    if (!this.newMessage.trim()) return;

    // Show user message
    this.messages.push({ sender: 'user', text: this.newMessage });

    // Set loading true
    this.loading = true;

    const prompt = this.newMessage;
    this.newMessage = '';

    // Call backend
    this.chatService.sendPrompt(prompt).subscribe((res) => {
      // Hide typing indicator
      this.loading = false;

      // Add bot response
      this.messages.push({ sender: 'assistant', text: res.reply });
    });
  }
}

