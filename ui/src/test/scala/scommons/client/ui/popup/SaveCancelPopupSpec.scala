package scommons.client.ui.popup

import scommons.client.ui.popup.SaveCancelPopupSpec._
import scommons.client.ui.{ButtonImagesCss, Buttons}
import scommons.react._
import scommons.react.test.TestSpec
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

class SaveCancelPopupSpec extends TestSpec with ShallowRendererUtils {

  it should "call onCancel function when cancel command" in {
    //given
    val onCancel = mockFunction[Unit]
    val props = TestPopupProps(title = "Test message", onCancel = onCancel)
    val component = shallowRender(<(SaveCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onCancel.expects()

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.CANCEL.command)
  }

  it should "call onSave function when SAVE command" in {
    //given
    val onSave = mockFunction[TestData, Unit]
    val props = TestPopupProps(onSave = onSave)
    val component = shallowRender(<(SaveCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onSave.expects(props.initialData)

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.SAVE.command)
  }

  it should "call onSave function with the new data when onEnter" in {
    //given
    val onSave = mockFunction[TestData, Unit]
    val props = TestPopupProps(onSave = onSave)
    val component = shallowRender(<(SaveCancelPopup())(^.wrapped := props)())
    val editProps = findComponentProps(component, TestEditPanel)
    val newData = props.initialData.copy(name = "updated")
    editProps.onChange(newData)

    //then
    onSave.expects(newData)

    //when
    editProps.onEnter()
  }

  it should "enable SAVE command when all the fields are filled" in {
    //given
    val props = TestPopupProps()
    val renderer = createRenderer()
    renderer.render(<(SaveCancelPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val prevEditProps = findComponentProps(comp, TestEditPanel)
    val newData = props.initialData.copy(name = "updated")

    //when
    prevEditProps.onChange(newData)

    //then
    val updatedComp = renderer.getRenderOutput()
    val editProps = findComponentProps(updatedComp, TestEditPanel)
    editProps.initialData shouldBe newData

    val modalProps = findComponentProps(updatedComp, Modal)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.SAVE.command, Buttons.CANCEL.command)
  }

  it should "disable SAVE command when one of the fields is emtpy" in {
    //given
    val props = TestPopupProps()
    val renderer = createRenderer()
    renderer.render(<(SaveCancelPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val prevEditProps = findComponentProps(comp, TestEditPanel)
    val newData = props.initialData.copy(name = "")

    //when
    prevEditProps.onChange(newData)

    //then
    val updatedComp = renderer.getRenderOutput()
    val editProps = findComponentProps(updatedComp, TestEditPanel)
    editProps.initialData shouldBe newData

    val modalProps = findComponentProps(updatedComp, Modal)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.CANCEL.command)
  }

  it should "render custom popup component" in {
    //given
    val props = TestPopupProps()
    val component = <(TestPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertComponent(result, SaveCancelPopup) { pProps =>
      pProps shouldBe props
    }
  }

  it should "render base popup component" in {
    //given
    val props = TestPopupProps()
    val component = <(SaveCancelPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertSaveCancelPopup(result, props)
  }

  it should "set requestFocus when onOpen" in {
    //given
    val props = TestPopupProps(title = "Test message")
    val renderer = createRenderer()
    renderer.render(<(SaveCancelPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    val editProps = findComponentProps(comp, TestEditPanel)
    editProps.requestFocus shouldBe false

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.getRenderOutput()
    val updatedTextProps = findComponentProps(updatedComp, TestEditPanel)
    updatedTextProps.requestFocus shouldBe true
  }

  it should "reset requestFocus and data when componentWillReceiveProps" in {
    //given
    val prevProps = TestPopupProps()
    val renderer = createRenderer()
    renderer.render(<(SaveCancelPopup())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    val editProps = findComponentProps(comp, TestEditPanel)
    editProps.initialData shouldBe prevProps.initialData
    editProps.requestFocus shouldBe false
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.SAVE.command, Buttons.CANCEL.command)
    modalProps.onOpen()
    val compV2 = renderer.getRenderOutput()
    val textPropsV2 = findComponentProps(compV2, TestEditPanel)
    textPropsV2.requestFocus shouldBe true
    val props = prevProps.copy(initialData = prevProps.initialData.copy(name = ""))

    //when
    renderer.render(<(SaveCancelPopup())(^.wrapped := props)())

    //then
    val compV3 = renderer.getRenderOutput()
    val modalPropsV3 = findComponentProps(compV3, Modal)
    modalPropsV3.actions.enabledCommands shouldBe Set(Buttons.CANCEL.command)

    val textPropsV3 = findComponentProps(compV3, TestEditPanel)
    textPropsV3.initialData shouldBe props.initialData
    textPropsV3.requestFocus shouldBe false
  }

  private def assertSaveCancelPopup(result: ShallowInstance, props: SaveCancelPopupProps): Unit = {
    val data = props.initialData
    val actionCommands =
      if (props.isSaveEnabled(data)) {
        Set(Buttons.SAVE.command, Buttons.CANCEL.command)
      }
      else Set(Buttons.CANCEL.command)

    assertComponent(result, Modal)({
      case ModalProps(show, header, buttons, actions, _, onClose, closable, _) =>
        show shouldBe props.show
        header shouldBe Some(props.title)
        buttons shouldBe List(Buttons.SAVE.copy(
          image = ButtonImagesCss.dbSave,
          disabledImage = ButtonImagesCss.dbSaveDisabled,
          primary = true
        ), Buttons.CANCEL)
        actions.enabledCommands shouldBe actionCommands
        onClose shouldBe props.onCancel
        closable shouldBe true
    }, { case List(editPanel) =>
      assertComponent(editPanel, TestEditPanel) {
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

  private case class TestPopupProps(show: Boolean = true,
                                    title: String = "Test Title",
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
