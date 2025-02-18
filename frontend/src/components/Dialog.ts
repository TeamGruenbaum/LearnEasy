export class Dialog {
    private readonly dialog: HTMLDialogElement
    private readonly titleElement: HTMLHeadingElement
    private readonly textElement: HTMLParagraphElement
    private readonly okButton: HTMLButtonElement
    private backdrop!: HTMLDivElement

    constructor(title: string, text: string, type: "modal" | "alert" = "modal") {
        const titelId = Math.random().toString(36).substring(7)
        const descriptionId = Math.random().toString(36).substring(7)

        this.dialog = document.createElement("dialog")
        this.dialog.ariaModal = "true"
        this.dialog.setAttribute("role", type === "modal" ? "modal" : "alertdialog")
        this.dialog.setAttribute("aria-labelledby", "dialog-title"+titelId)
        this.dialog.setAttribute("aria-describedby", "dialog-description-"+descriptionId)
        this.dialog.classList.add("p-4", "bg-light", "shadow", "rounded-3", "border-0")

        this.titleElement = document.createElement("h2")
        this.titleElement.setAttribute("id", "dialog-title"+titelId)
        this.titleElement.textContent = title
        this.titleElement.classList.add("fs-2", "mb-3", "text-center")
        this.dialog.appendChild(this.titleElement)

        this.textElement = document.createElement("p")
        this.textElement.setAttribute("id", "dialog-description-"+descriptionId)
        this.textElement.textContent = text
        this.textElement.classList.add("mb-4", "text-center")
        this.dialog.appendChild(this.textElement)

        this.okButton = document.createElement("button")
        this.okButton.textContent = "OK"
        this.okButton.tabIndex = 0
        this.okButton.style.minWidth = "150px"
        this.okButton.addEventListener("click", () => {
            this.dialog.close()
            if (this.backdrop) document.body.removeChild(this.backdrop)
            this.dialog.remove()
        })
        this.okButton.className = "btn btn-primary d-block mx-auto"
        this.dialog.appendChild(this.okButton)
    }

    public show() {
        document.body.appendChild(this.dialog)
        this.dialog.showModal()
        this.backdrop = document.createElement("div")
        this.backdrop.classList.add("modal-backdrop", "fade", "show")
        document.body.appendChild(this.backdrop)
    }

    static Common = {
        validationFailed: new Dialog("Fehler aufgetreten", "Bevor es weitergehen kann, müssen alle Felder korrekt ausgefüllt sein.", "alert"),
        errorOccurred: (message: string) => new Dialog("Fehler aufgetreten", message, "alert")
    }
}