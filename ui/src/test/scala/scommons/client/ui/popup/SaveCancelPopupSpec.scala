package scommons.client.ui.popup

import scommons.client.ui.popup.SaveCancelPopup._
import scommons.client.ui.popup.SaveCancelPopupSpec._
import scommons.client.ui.{ButtonImagesCss, Buttons}
import scommons.react._
import scommons.react.test._

class SaveCancelPopupSpec extends TestSpec with TestRendererUtils {

  SaveCancelPopup.modalComp = () => "Modal".asInstanceOf[ReactClass]

  it should "call onCancel function when cancel command" in {
    //given
    val onCancel = mockFunction[Unit]
    val props = TestPopupProps(title = "Test message", onCancel = onCancel)
    val component = testRender(<(SaveCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, modalComp)

    //then
    onCancel.expects()

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.CANCEL.command)
  }

  it should "call onSave function when SAVE command" in {
    //given
    val onSave = mockFunction[TestData, Unit]
    val props = TestPopupProps(onSave = onSave)
    val component = testRender(<(SaveCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, modalComp)

    //then
    onSave.expects(props.initialData)

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.SAVE.command)
  }

  it should "call onSave function with the new data when onEnter" in {
    //given
    val onSave = mockFunction[TestData, Unit]
    val props = TestPopupProps(onSave = onSave)
    val renderer = createTestRenderer(<(SaveCancelPopup())(^.wrapped := props)())
    val newData = props.initialData.copy(name = "updated")
    findComponentProps(renderer.root, TestEditPanel).onChange(newData)

    //then
    onSave.expects(newData)

    //when
    findComponentProps(renderer.root, TestEditPanel).onEnter()
  }

  it should "enable SAVE command when all the fields are filled" in {
    //given
    val props = TestPopupProps()
    val renderer = createTestRenderer(<(SaveCancelPopup())(^.wrapped := props)())
    val comp = renderer.root
    val prevEditProps = findComponentProps(comp, TestEditPanel)
    val newData = props.initialData.copy(name = "updated")

    //when
    prevEditProps.onChange(newData)

    //then
    val updatedComp = renderer.root
    val editProps = findComponentProps(updatedComp, TestEditPanel)
    editProps.initialData shouldBe newData

    val modalProps = findComponentProps(updatedComp, modalComp)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.SAVE.command, Buttons.CANCEL.command)
  }

  it should "disable SAVE command when one of the fields is emtpy" in {
    //given
    val props = TestPopupProps()
    val renderer = createTestRenderer(<(SaveCancelPopup())(^.wrapped := props)())
    val comp = renderer.root
    val prevEditProps = findComponentProps(comp, TestEditPanel)
    val newData = props.initialData.copy(name = "")

    //when
    prevEditProps.onChange(newData)

    //then
    val updatedComp = renderer.root
    val editProps = findComponentProps(updatedComp, TestEditPanel)
    editProps.initialData shouldBe newData

    val modalProps = findComponentProps(updatedComp, modalComp)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.CANCEL.command)
  }

  it should "render custom popup component" in {
    //given
    val props = TestPopupProps()
    val component = <(TestPopup())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertTestComponent(result, SaveCancelPopup) { pProps =>
      pProps shouldBe props
    }
  }

  it should "render base popup component" in {
    //given
    val props = TestPopupProps()
    val component = <(SaveCancelPopup())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertSaveCancelPopup(result, props)
  }

  it should "set requestFocus when onOpen" in {
    //given
    val props = TestPopupProps(title = "Test message")
    val renderer = createTestRenderer(<(SaveCancelPopup())(^.wrapped := props)())
    val comp = renderer.root
    val modalProps = findComponentProps(comp, modalComp)
    val editProps = findComponentProps(comp, TestEditPanel)
    editProps.requestFocus shouldBe false

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.root
    val updatedTextProps = findComponentProps(updatedComp, TestEditPanel)
    updatedTextProps.requestFocus shouldBe true
  }

  private def assertSaveCancelPopup(result: TestInstance, props: SaveCancelPopupProps): Unit = {
    val data = props.initialData
    val actionCommands =
      if (props.isSaveEnabled(data)) {
        Set(Buttons.SAVE.command, Buttons.CANCEL.command)
      }
      else Set(Buttons.CANCEL.command)

    assertTestComponent(result, modalComp)({
      case ModalProps(header, buttons, actions, _, onClose, closable, _) =>
        header shouldBe Some(props.title)
        buttons shouldBe List(Buttons.SAVE.copy(
          image = ButtonImagesCss.dbSave,
          disabledImage = ButtonImagesCss.dbSaveDisabled,
          primary = true
        ), Buttons.CANCEL)
        actions.enabledCommands shouldBe actionCommands
        onClose shouldBe props.onCancel
        closable shouldBe true
    }, inside(_) { case List(editPanel) =>
      assertTestComponent(editPanel, TestEditPanel) {
        case TestEditPanelProps(initialData, requestFocus, _, _) =>
          initialData shouldBe props.initialData
          requestFocus shouldBe false
      }
    })
  }
}

object SaveCancelPopupSpec {

  private case class TestData(name: String)

  private case class TestEditPanelProps(initialData: TestData,
                                        requestFocus: Boolean,
                                        onChange: TestData => Unit,
                                        onEnter: () => Unit)

  private object TestEditPanel extends FunctionComponent[TestEditPanelProps] {

    protected def render(props: Props): ReactElement = {
      <.div()(s"Name: ${props.wrapped.initialData.name}")
    }
  }

  private case class TestPopupProps(title: String = "Test Title",
                                    initialData: TestData = TestData("test name"),
                                    onSave: TestData => Unit = _ => (),
                                    onCancel: () => Unit = () => ()) extends SaveCancelPopupProps {

    type DataType = TestData

    def isSaveEnabled(data: TestData): Boolean = {
      data.name.trim.nonEmpty
    }

    def render(data: TestData,
               requestFocus: Boolean,
               onChange: TestData => Unit,
               onSave: () => Unit): ReactElement = {

      <(TestEditPanel())(^.wrapped := TestEditPanelProps(
        initialData = data,
        requestFocus = requestFocus,
        onChange = onChange,
        onEnter = onSave
      ))()
    }
  }

  private object TestPopup extends SaveCancelPopup[TestPopupProps]
}
